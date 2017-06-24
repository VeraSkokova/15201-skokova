package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ObjectStreamConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public abstract class ChatMessage implements Serializable {
    private int type;
    private String message;
    private ObjectStreamConnectedClient objectStreamConnectedClient;

    public ChatMessage() {}

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public void process(Server server, ObjectStreamConnectedClient objectStreamConnectedClient) {}

    public ObjectStreamConnectedClient getObjectStreamConnectedClient() {
        return objectStreamConnectedClient;
    }
}