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

    ObjectStreamClient() {
    }

    public static void main(String[] args) {
        Client client = new ObjectStreamClient();
        ClientFrame clientFrame = new ClientFrame(client);
        client.addHandler(clientFrame.new MessageUpdater());
    }

    public void start() {
        try {
            if (!isConnected) {
                createSocket();

                this.inputStream = new ObjectInputStream(socket.getInputStream());
                this.outputStream = new ObjectOutputStream(socket.getOutputStream());

                this.inThread = new Thread(new ObjectStreamClient.ReadFromServer());
                this.outThread = new Thread(new ObjectStreamClient.WriteToServer());

                this.inThread.start();
                this.outThread.start();
            }
            messages.add(new LoginMessage(this));
        } catch (SocketTimeoutException e) {
            logger.error("Socket timeout exceeded");
            clientConnectedHandler.handle(false);
        } catch (IOException e) {
            logger.error("Error in connection to server:" + e.getMessage());
            clientConnectedHandler.handle(false);
        }
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
        } catch (IOException e) {
            logger.error("Exception writing to server: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            isConnected = false;
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    class ReadFromServer implements Runnable {
        @Override
        public void run() {
            while (true) {
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
