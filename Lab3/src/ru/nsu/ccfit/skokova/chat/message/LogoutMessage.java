package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class LogoutMessage extends ChatMessage{

    public LogoutMessage() {}

    public void process(Server server, ConnectedClient connectedClient) {
        server.display(connectedClient.getUsername() + " disconnected with a LOGOUT message.");
        connectedClient.interrupt();
        connectedClient.close();
        server.removeClient(connectedClient);
    }
}
