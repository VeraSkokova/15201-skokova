package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected static final Logger logger = LogManager.getLogger(ChatMessage.class);

    protected String username;

    public Message() {}

    public Message(ConnectedClient connectedClient) {
        this.username = connectedClient.getUsername();
    }

    public Message(String username) {
        this.username = username;
    }

    public void process(Server server, ConnectedClient connectedClient) {}

    public abstract String getMessage();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
