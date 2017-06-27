package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.Server;


public class LoginMessage extends ChatMessage {
    private String username;

    public LoginMessage(Client client) {
        this.username = client.getUsername();
    }

    public LoginMessage(String username, String type) {
        this.username = username;
    }

    public void process(Server server) {
        if (server.getUsernames().contains(this.username)) {
            server.sendMessage(new LoginError("This username is busy, try another"), connectedClient);
            connectedClient.interrupt();
            connectedClient.close();
            server.removeClient(connectedClient);
        } else {
            connectedClient.setUsername(this.username);
            connectedClient.setSessionId(server.setUserSessionId());
            server.addClient(connectedClient);
            server.broadcast(new NewClientMessage(connectedClient.getUsername() + " logged in"));
            server.getUsernames().add(this.username);
            server.sendMessage(new LoginSuccess(connectedClient.getSessionId()), connectedClient);
        }
    }
}
