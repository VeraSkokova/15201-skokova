package ru.nsu.ccfit.skokova.chat;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.skokova.chat.gui.ClientFrame;
import ru.nsu.ccfit.skokova.chat.message.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class XMLClient extends Client {
    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private XMLMessageCreator xmlMessageCreator = new XMLMessageCreator();

    XMLClient() {}

    XMLClient(String server, int port, String username) {
        super(server, port, username);
    }

    public static void main(String[] args) {
        Client client = new XMLClient();
        ClientFrame clientFrame = new ClientFrame(client);
        client.addHandler(clientFrame.new MessageUpdater());

        //client.start();
    }

    public void start() {
        try {
            createSocket();
        } catch (SocketTimeoutException e) {
            logger.error("Socket timeout exceeded");
            clientConnectedHandler.handle(false);
            return;
        } catch (IOException e) {
            logger.error("Error in connection to server:" + e.getMessage());
            clientConnectedHandler.handle(false);
            return;
        }

        messages.add(xmlMessageCreator.createLoginMessage(username, "XML"));

        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Exception creating new Input/output Streams: " + e.getMessage());
        } catch (NullPointerException e) {
            notifyValueChanged(new LoginError("Server is not opened"));
            return;
        }

        this.inThread = new Thread(new XMLClient.ReadFromServer());
        this.outThread = new Thread(new XMLClient.WriteToSever());

        this.inThread.start();
        this.outThread.start();

    }

    private void display(String msg) {
        System.out.println(msg);
    }

    public void sendTextMessage(String message) {
        messages.add(xmlMessageCreator.createClientMessage(message, Integer.toString(sessionId)));
    }

    public void sendLogoutMessage() {
        messages.add(xmlMessageCreator.createLogoutMessage(Integer.toString(this.sessionId)));
    }

    public void sendUserListMessage() {
        messages.add(xmlMessageCreator.createUserListRequestMessage(Integer.toString(sessionId)));
    }

    private void sendMessage(String msg) {
        try {
            outputStream.writeInt(msg.getBytes(StandardCharsets.UTF_8).length);
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch(IOException e) {
            display("Exception writing to server: " + e.getMessage());
        }
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void disconnect() {
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null) {
                outputStream.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    class ReadFromServer implements Runnable {
        XMLMessageInterpreter xmlMessageInterpreter = new XMLMessageInterpreter(XMLClient.this);
        @Override
        public void run() {
            try {
                ByteReader byteReader = new ByteReader(inputStream);
                while (true) {
                    byte[] messageBytes = byteReader.readMessage();
                    if (messageBytes == null) {
                        logger.error("Message hasn't been read");
                    } else {
                        String msg = new String(messageBytes);
                        ServerMessage serverMessage = xmlMessageInterpreter.interpret(msg);
                        logger.debug("XMLClient read " + serverMessage.getClass());
                        serverMessage.interpret(XMLClient.this);
                    }
                }
            } catch (IOException e) {
                logger.info("Server has closed the connection");
                setLoggedIn(false);
                clientConnectedHandler.handle(false);
                disconnect();
                interrupt();
            }
        }
    }

    class WriteToSever implements Runnable {
        @Override
        public void run() {
            XMLToMessage xmlToMessage = new XMLToMessage();
            try {
                while (true) {
                    String message = (String) messages.take();
                    sendMessage(message);
                    Message msg = xmlToMessage.parseMessage(new InputSource(new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8))));
                    msg.setUsername(username);
                    sentMessages.add(msg);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ParserConfigurationException | IOException | SAXException e) {
                logger.error("Can't parse message");
            }
        }
    }
}

