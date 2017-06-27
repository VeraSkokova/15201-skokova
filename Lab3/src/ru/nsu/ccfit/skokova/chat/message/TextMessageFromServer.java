package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public class TextMessageFromServer extends ServerMessage implements Serializable {
    private String sender;
    private String sentMessage;

    public TextMessageFromServer(String message, String sender) {
        super(sender + ": " + message);
        this.sender = sender;
        this.sentMessage = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void interpret(Client client) {}

    public String getSender() {
        return sender;
    }

    public String getSentMessage() {
        return sentMessage;
    }
}
