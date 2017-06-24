package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ObjectStreamConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

public class LogoutMessage extends ChatMessage {

    public LogoutMessage() {}

    public void process(Server server, ObjectStreamConnectedClient objectStreamConnectedClient) {
        server.display(objectStreamConnectedClient.getUsername() + " disconnected with a LOGOUT message.");
        objectStreamConnectedClient.interrupt();
        objectStreamConnectedClient.close();
        server.removeClient(objectStreamConnectedClient);
        TextMessage textMessage = new TextMessage(objectStreamConnectedClient.getUsername() + " logged out");
        server.broadcast(textMessage);
        server.saveMessage(textMessage);
    }
}
