package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public abstract class ChatMessage implements Serializable {
    private int type;
    private String message;

    public ChatMessage() {}

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public void process(Server server, ConnectedClient connectedClient) {}
}