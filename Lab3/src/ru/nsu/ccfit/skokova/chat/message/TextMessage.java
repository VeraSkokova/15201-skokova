package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class TextMessage extends ChatMessage {
    private String message;

    public TextMessage(String message) {
        this.message = message;
    }

    public void process(Server server, ConnectedClient connectedClient) {
        TextMessage textMessage = new TextMessage(connectedClient.getUsername() + ": " + message);
        server.broadcast(textMessage);
        server.saveMessage(textMessage);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
