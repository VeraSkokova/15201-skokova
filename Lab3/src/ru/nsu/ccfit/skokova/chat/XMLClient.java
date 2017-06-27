package ru.nsu.ccfit.skokova.chat;

import ru.nsu.ccfit.skokova.chat.gui.ClientFrame;
import ru.nsu.ccfit.skokova.chat.message.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class XMLClient extends Client {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private XMLMessageCreator xmlMessageCreator = new XMLMessageCreator();

    XMLClient() {}

    XMLClient(String server, int port, String username) {
        super(server, port, username);
    }

    public void start() {
        try {
            socket = new Socket(server, port);
            logger.debug("Client server: " + server);
            logger.debug("Client port: " + port);
        } catch (Exception ec) {
            logger.error("Error in connection to server:" + ec.getMessage());
        }

        xmlMessages.add(xmlMessageCreator.createLoginMessage(username, "XML"));

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
        xmlMessages.add(xmlMessageCreator.createClientMessage(message, Integer.toString(sessionId)));
        //sendMessage(xmlMessageCreator.createClientMessage(message, Integer.toString(sessionId))); //!!!!!!!!!!
    }

    public void sendLogoutMessage() {
        xmlMessages.add(xmlMessageCreator.createLogoutMessage(Integer.toString(this.sessionId)));
        //sendMessage(xmlMessageCreator.createLogoutMessage(this.username));
    }

    public void sendUserListMessage() {
        xmlMessages.add(xmlMessageCreator.createUserListRequestMessage(Integer.toString(sessionId)));
        //sendMessage(xmlMessageCreator.createUserListRequestMessage(Integer.toString(sessionId))); //!!!!!!!!!!!!
    }

    private void sendMessage(String msg) {
        try {
            logger.debug("Writing " + msg);
            outputStream.writeInt(msg.getBytes(StandardCharsets.UTF_8).length);
            logger.debug("Length: " + msg.getBytes(StandardCharsets.UTF_8).length);
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

    public static void main(String[] args) {
        int portNumber = 1500;
        String serverAddress = "localhost";
        String userName = "Anonymous";

        Client client = new XMLClient(serverAddress, portNumber, userName);
        ClientFrame clientFrame = new ClientFrame(client);
        client.addHandler(clientFrame.new MessageUpdater());

        //client.start();
    }

    class ReadFromServer implements Runnable {
        XMLMessageInterpretator xmlMessageInterpretator = new XMLMessageInterpretator(XMLClient.this);
        @Override
        public void run() {
            try {
                ByteReader byteReader = new ByteReader(inputStream);
                while (!Thread.interrupted()) {
                    byte[] messageBytes = byteReader.readMessage();
                    if (messageBytes == null) {
                        logger.error("Message hasn't been read");
                    } else {
                        String msg = new String(messageBytes);
                        ServerMessage serverMessage = xmlMessageInterpretator.interpret(msg);
                        serverMessage.interpret(XMLClient.this);
                        notifyValueChanged(serverMessage);
                    }
                }
            } catch (IOException e) {
                logger.info("Server has closed the connection");
                disconnect();
                interrupt();
            }
        }
    }

    class WriteToSever implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    String message = xmlMessages.take();
                    sendMessage(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

