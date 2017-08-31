package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;


public class LoginMessage extends ChatMessage {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private String username;

    public LoginMessage(Client client) {
        this.username = client.getUsername();
    }

    public LoginMessage(String username, String type) {
        this.username = username;
    }

    public void process(Server server, ConnectedClient connectedClient) {
        if (server.getUsernames().contains(this.username)) {
            server.sendMessage(new LoginError("This username is busy, try another"), connectedClient);
            //connectedClient.interrupt();
            //connectedClient.close();
            server.removeClient(connectedClient);
        } else {
            connectedClient.setUsername(this.username);
            connectedClient.setSessionId(server.setUserSessionId());
            server.sendMessage(new LoginSuccess(connectedClient.getSessionId()), connectedClient);
            server.sendMessageHistory(connectedClient);
            server.addClient(connectedClient);
            NewClientMessage newClientMessage = new NewClientMessage(connectedClient.getUsername());
            //newClientMessage.setUsername(this.username);
            logger.debug("Preparing to broadcast " + newClientMessage.toString());
            server.broadcast(newClientMessage);
            server.getUsernames().add(this.username);
        }
    }
}
