package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.ObjectStreamConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.text.SimpleDateFormat;

public class UserListMessage extends ChatMessage {
    public UserListMessage() {}

    public void process(Server server, ObjectStreamConnectedClient objectStreamConnectedClient) {
        server.sendMessage(new TextMessage("List of the users connected at " + new SimpleDateFormat() + "\n"), objectStreamConnectedClient); //TODO : сделать красиво
        for(int i = 0; i < server.getConnectedClients().size(); ++i) {
            ConnectedClient ct = server.getConnectedClients().get(i);
            server.sendMessage(new TextMessage((i+1) + ") " + ct.getUsername() + " since " + ct.getDate()), objectStreamConnectedClient);
        }
    }
}
