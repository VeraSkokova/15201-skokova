package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected static final Logger logger = LogManager.getLogger(ChatMessage.class);

    protected ConnectedClient connectedClient;

    public Message() {}

    public Message(ConnectedClient connectedClient) {
        this.connectedClient = connectedClient;
    }

    public void process(Server server) {}

    public void setConnectedClient(ConnectedClient connectedClient) {
        this.connectedClient = connectedClient;
    }

    public abstract String getMessage();
}
