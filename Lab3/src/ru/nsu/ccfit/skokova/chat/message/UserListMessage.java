package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class UserListMessage extends ChatMessage implements Serializable {
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();

    public UserListMessage() {}

    public UserListMessage(int sessionID) {
        super(sessionID);
    }

    public void process(Server server) {
            if (this.getSessionId() != connectedClient.getSessionId()) {
                server.sendMessage(new UserListError("Authentication error"), connectedClient);
            } else {
                String message = "List of the users\n";
                for (int i = 0; i < server.getConnectedClients().size(); ++i) {
                    ConnectedClient connectedClient = server.getConnectedClients().get(i);
                    message += Integer.toString(i + 1)  + "." + " " + connectedClient.getUsername() + " " + connectedClient.getType() + "\n";
                    usernames.add(connectedClient.getUsername());
                    types.add(connectedClient.getType());
                }
                UserListSuccess userListSuccess = new UserListSuccess(message);
                userListSuccess.setUsernames(usernames);
                userListSuccess.setTypes(types);
                server.sendMessage(userListSuccess, connectedClient);
            }
    }

    private String userXMLInfo(ConnectedClient connectedClient) {
        return "<user>\n" +
                "<name>" + connectedClient.getUsername() + "</name>\n" +
                "<type>" + connectedClient.getType() + "</type>\n" +
                "</user>\n";
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
}
