package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ObjectStreamConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class TextMessage extends ChatMessage {
    private String message;

    public TextMessage(String message) {
        this.message = message;
    }

    public void process(Server server, ObjectStreamConnectedClient objectStreamConnectedClient) {
        TextMessage textMessage = new TextMessage(objectStreamConnectedClient.getUsername() + ": " + message);
        server.broadcast(textMessage);
        server.saveMessage(textMessage);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
