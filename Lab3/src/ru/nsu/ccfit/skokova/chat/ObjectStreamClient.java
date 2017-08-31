package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.gui.ClientFrame;
import ru.nsu.ccfit.skokova.chat.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;

public class ObjectStreamClient extends Client {
    private static final Logger logger = LogManager.getLogger(ObjectStreamClient.class);

    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    ObjectStreamClient() {}

    ObjectStreamClient(String server, int port, String username) {
        super(server, port, username);
    }

    public static void main(String[] args) {

        Client client = new ObjectStreamClient();
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

        try {
            logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (NullPointerException e) {
            notifyValueChanged(new LoginError("Server is not opened"));
            return;
        }

        messages.add(new LoginMessage(this));

        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Exception creating new Input/output Streams: " + e.getMessage());
        }

        this.inThread = new Thread(new ObjectStreamClient.WriteToServer());
        this.outThread = new Thread(new ObjectStreamClient.ReadFromServer());

        this.inThread.start();
        this.outThread.start();
    }

    public void sendTextMessage(String message) {
        TextMessageToServer msg = new TextMessageToServer(message);
        msg.setSessionId(this.getSessionId());
        messages.add(msg);
    }

    public void sendLogoutMessage() {
        LogoutMessage msg = new LogoutMessage();
        msg.setSessionId(this.sessionId);
        messages.add(msg);
    }

    public void sendUserListMessage() {
        UserListMessage msg = new UserListMessage();
        msg.setSessionId(this.sessionId);
        messages.add(msg);
    }

    private void sendMessage(Message msg) {
        try {
            msg.setUsername(this.username);
            logger.info(username + " send " + msg.getMessage());
            outputStream.writeObject(msg);
        } catch(IOException e) {
            logger.error("Exception writing to server: " + e.getMessage());
        }
    }

    public void disconnect() {}

    public void setServer(String server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPort(int port) {
        this.port = port;
    }

    class ReadFromServer implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    ServerMessage message = (ServerMessage) inputStream.readObject();
                    logger.debug("ObjectStreamClient read " + message.getMessage());
                    message.interpret(ObjectStreamClient.this);
                } catch (IOException e) {
                    logger.info("Server has closed the connection");
                    setLoggedIn(false);
                    clientConnectedHandler.handle(false);
                    disconnect();
                    interrupt();
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    class WriteToServer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) messages.take();
                    sendMessage(message);
                    message.setUsername(username);
                    sentMessages.add(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
