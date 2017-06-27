package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.Server;


public class LoginMessage extends ChatMessage {
    private String username;
    private String type;

    public LoginMessage(Client client) {
        this.username = client.getUsername();
    }

    public LoginMessage(String username, String type) {
        this.username = username;
        this.type = type;
    }

    public void process(Server server) {
        logger.debug("Here!!!");
        if (server.getUsernames().contains(this.username)) {
            server.sendMessage(new LoginError("This username is busy, try another"), connectedClient);
            connectedClient.interrupt();
            connectedClient.close();
            server.removeClient(connectedClient);
        } else {
            logger.debug(this.username);
            connectedClient.setUsername(this.username);
            connectedClient.setSessionId(server.setUserSessionId());
            server.addClient(connectedClient);
            server.broadcast(new NewClientMessage(connectedClient.getUsername() + " logged in"));
            server.getUsernames().add(this.username);
            server.sendMessage(new LoginSuccess(connectedClient.getSessionId()), connectedClient);
        }
    }
}
