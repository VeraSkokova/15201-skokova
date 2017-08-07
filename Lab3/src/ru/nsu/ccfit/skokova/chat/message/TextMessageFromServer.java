package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

import java.io.Serializable;

public class TextMessageFromServer extends ServerMessage implements Serializable {
    private String sentMessage;

    public TextMessageFromServer(String message, String sender) {
        super(sender + ": " + message);
        this.sentMessage = message;
        this.username = sender;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void interpret(Client client) {}

    public String getSentMessage() {
        return sentMessage;
    }
}
