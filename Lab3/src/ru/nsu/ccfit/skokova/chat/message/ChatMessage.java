package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public abstract class ChatMessage extends Message {
    private int type;
    private String message;
    protected int sessionId;

    public ChatMessage() {}

    public ChatMessage(ConnectedClient connectedClient) {
        super(connectedClient);
    }

    public ChatMessage(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public void process(Server server, ConnectedClient connectedClient) {}

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}