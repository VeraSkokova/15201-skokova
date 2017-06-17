package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class TextMessage extends ChatMessage {
    private String message;

    public TextMessage(String message) {
        this.message = message;
    }

    public void process(Server server, ConnectedClient connectedClient) {
        server.broadcast(new TextMessage(connectedClient.getUsername() + ": " + message));
    }

    @Override
    public String getMessage() {
        return message;
    }
}
