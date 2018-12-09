package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public abstract class ChatMessage extends Message {
    protected String message;
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