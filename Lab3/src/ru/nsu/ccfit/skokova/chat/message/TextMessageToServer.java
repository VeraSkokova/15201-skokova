package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class TextMessageToServer extends ChatMessage {
    private static final Logger logger = LogManager.getLogger(Server.class);
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
            logger.debug("Processing " + message + " from " + connectedClient.getUsername());
            server.sendMessage(new TextMessageToServerSuccess(""), connectedClient);
            TextMessageFromServer textMessage = new TextMessageFromServer(message, connectedClient.getUsername());
            logger.debug("Preparing to broadcast " + textMessage.getMessage() + " from " + textMessage.getUsername()); //TODO : remove
            server.broadcast(textMessage);
            server.saveMessage(textMessage);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
