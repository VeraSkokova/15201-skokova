package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.gui.ValueChangedHandler;
import ru.nsu.ccfit.skokova.chat.message.LoginError;
import ru.nsu.ccfit.skokova.chat.message.Message;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Client {
    private static final int MESSAGES_COUNT = 10000;
    protected Socket socket;
    protected boolean isLoggedIn;

    protected String server;
    protected String username;
    protected int sessionId;
    protected int port;

    protected Thread inThread;
    protected Thread outThread;

    protected BlockingQueue<Message> messages = new ArrayBlockingQueue<>(MESSAGES_COUNT); //TODO : queue of objects?
    protected BlockingQueue<String> xmlMessages = new ArrayBlockingQueue<>(MESSAGES_COUNT);

    protected BlockingQueue<Object> sentMessages = new ArrayBlockingQueue<>(MESSAGES_COUNT);

    protected ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

    protected Logger logger = LogManager.getLogger(Client.class);

    Client() {
    }

    Client(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }


    public void addHandler(ValueChangedHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
    }

    public void notifyValueChanged(Object value) {
        for (ValueChangedHandler handler : handlers) {
            handler.handle(value);
        }
    }

    public void sendErrorMessage() {
    }

    public void start() {
        try {
            socket = new Socket(server, port);
        } catch (Exception e) {
            logger.error("Error in connection to server:" + e.getMessage());
        }

        try {
            String message = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
            logger.info(message);
        } catch (NullPointerException e) {
            notifyValueChanged(new LoginError("Server is not opened"));
            return;
        }
    }

    public void sendTextMessage(String message) {
    }

    public void sendLogoutMessage() {
    }

    public void sendUserListMessage() {
    }

    public void interrupt() {
        inThread.interrupt();
        outThread.interrupt();
    }

    public void disconnect() {
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BlockingQueue<Object> getSentMessages() {
        return sentMessages;
    }
}
