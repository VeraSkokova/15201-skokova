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
            server.sendMessage(new LogoutSuccess(), connectedClient);
            server.getUsernames().remove(connectedClient.getUsername());
            //connectedClient.interrupt();
            server.removeClient(connectedClient);
            ClientLoggedOutMessage textMessageFromServer = new ClientLoggedOutMessage(connectedClient.getUsername());
            server.broadcast(textMessageFromServer);
            server.saveMessage(textMessageFromServer);
        }
    }

    @Override
    public String toString() {
        return username + " logged out";
    }
}
