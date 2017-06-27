package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public class TextMessageToServer extends ChatMessage implements Serializable {
    private String message;

    public TextMessageToServer(String message) {
        this.message = message;
    }

    public TextMessageToServer(String message, int sessionId) {
        super(sessionId);
        this.message = message;
    }

    public void process(Server server) {
        if (this.getSessionId() != connectedClient.getSessionId()) {
            server.sendMessage(new TextMessageToServerError("Authentication error"), connectedClient);
            logger.debug(this.getSessionId() + " != " + connectedClient.getSessionId());
        } else {
            server.sendMessage(new TextMessageToServerSuccess(""), connectedClient);
            TextMessageFromServer textMessage = new TextMessageFromServer(message, connectedClient.getUsername());
            logger.debug("Processing: created " + textMessage + " " + textMessage.getMessage());
            server.broadcast(textMessage);
            server.saveMessage(textMessage);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
