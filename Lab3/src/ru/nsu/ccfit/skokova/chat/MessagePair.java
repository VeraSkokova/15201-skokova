package ru.nsu.ccfit.skokova.chat;

public class MessagePair {
    private String message;
    private int sessionID;

    public MessagePair(String message, int sessionID) {
        this.message = message;
        this.sessionID = sessionID;
    }

    public String getMessage() {
        return message;
    }

    public int getSessionID() {
        return sessionID;
    }
}
