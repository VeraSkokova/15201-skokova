package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.message.ChatMessage;
import ru.nsu.ccfit.skokova.chat.message.Message;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class ConnectedClient {
    protected static final int MAX_MSG_COUNT = 10000;
    protected int sessionId;
    protected Socket socket;
    protected Server server;

    protected String username;
    protected String date;
    protected boolean isValid;
    protected String type;

    protected static final Logger logger = LogManager.getLogger(ObjectStreamConnectedClient.class);

    protected BlockingQueue<Message> messages = new ArrayBlockingQueue<>(MAX_MSG_COUNT);
    protected BlockingQueue<String> xmlMessages = new ArrayBlockingQueue<>(MAX_MSG_COUNT);

    protected Thread readerThread;
    protected Thread writerThread;

    public ConnectedClient() {}

    public ConnectedClient(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void run() {}

    public void interrupt() {}

    public void close() {}

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public BlockingQueue<Message> getMessages() {
        return messages;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getType() {
        return type;
    }

    public void login(Server server) {}

}
