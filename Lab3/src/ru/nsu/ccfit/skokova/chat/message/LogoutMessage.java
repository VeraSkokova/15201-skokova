package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public class LogoutMessage extends ChatMessage implements Serializable {

    public LogoutMessage() {}

    public void process(Server server) {
        if (this.getSessionId() != connectedClient.getSessionId()) {
            server.sendMessage(new LogoutError("Authentication error"), connectedClient);
        } else {
            server.display(connectedClient.getUsername() + " disconnected with a LOGOUT message.");
            server.sendMessage(new LogoutSuccess(), connectedClient);
            server.getUsernames().remove(connectedClient.getUsername());
            connectedClient.interrupt();
            connectedClient.close();
            server.removeClient(connectedClient);
            ClientLoggedOutMessage textMessageFromServer = new ClientLoggedOutMessage(connectedClient.getUsername() + " logged out");
            server.broadcast(textMessageFromServer);
            server.saveMessage(textMessageFromServer);
        }
    }
}
