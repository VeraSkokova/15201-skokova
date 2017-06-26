package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public abstract class ChatMessage extends Message {
    private int type;
    private String message;
    protected ConnectedClient connectedClient;
    protected int sessionId;

    public ChatMessage() {}

    public ChatMessage(ConnectedClient connectedClient) {
        super(connectedClient);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public void process(Server server) {}

    public ConnectedClient getConnectedClient() {
        return connectedClient;
    }

    public void setConnectedClient(ConnectedClient connectedClient) {
        this.connectedClient = connectedClient;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}