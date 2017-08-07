package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class TextMessageToServer extends ChatMessage {
    private String message;

    public TextMessageToServer(String message) {
        this.message = message;
    }

    public TextMessageToServer(String message, int sessionId) {
        super(sessionId);
        this.message = message;
    }

    public void process(Server server, ConnectedClient connectedClient) {
        if (this.getSessionId() != connectedClient.getSessionId()) {
            server.sendMessage(new TextMessageToServerError("Authentication error"), connectedClient);
            logger.debug(this.getSessionId() + " != " + connectedClient.getSessionId());
        } else {
            server.sendMessage(new TextMessageToServerSuccess(""), connectedClient);
            TextMessageFromServer textMessage = new TextMessageFromServer(message, connectedClient.getUsername());
            server.broadcast(textMessage);
            server.saveMessage(textMessage);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
