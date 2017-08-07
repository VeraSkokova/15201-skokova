package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class LogoutMessage extends ChatMessage {

    public LogoutMessage() {}

    public LogoutMessage(int sessionId) {
        super(sessionId);
    }

    public void process(Server server, ConnectedClient connectedClient) {
        if (this.getSessionId() != connectedClient.getSessionId()) {
            server.sendMessage(new LogoutError("Authentication error"), connectedClient);
        } else {
            server.display(connectedClient.getUsername() + " disconnected with a LOGOUT message.");
            ClientLoggedOutMessage textMessageFromServer = new ClientLoggedOutMessage(connectedClient.getUsername());
            server.sendMessage(textMessageFromServer, connectedClient);
            server.sendMessage(new LogoutSuccess(), connectedClient);
            server.getUsernames().remove(connectedClient.getUsername());
            connectedClient.interrupt();
            connectedClient.close();
            server.removeClient(connectedClient);
            server.broadcast(textMessageFromServer);
            server.saveMessage(textMessageFromServer);
        }
    }
}
