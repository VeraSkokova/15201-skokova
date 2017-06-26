package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.ConnectedClient;
import ru.nsu.ccfit.skokova.chat.Server;

import java.io.Serializable;

public class UserListMessage extends ChatMessage implements Serializable {
    public UserListMessage() {}

    public void process(Server server) {
        //if (connectedClient.getType().equals("ObjectStream")) {
            if (this.getSessionId() != connectedClient.getSessionId()) {
                server.sendMessage(new UserListError("Authentication error"), connectedClient);
            } else {
                String message = "List of the users\n";
                for (int i = 0; i < server.getConnectedClients().size(); ++i) {
                    ConnectedClient ct = server.getConnectedClients().get(i);
                    message += Integer.toString(i + 1)  + "." + " " + ct.getUsername() + " " + ct.getType() + "\n";
                }
                server.sendMessage(new UserListSuccess(message), connectedClient);
            }
        /*} else {
            String answer = "<success>\n<listusers>\n";
            for (ConnectedClient client : server.getConnectedClients()) {
                answer += userXMLInfo(client);
            }
            answer += "</listusers>\n</success>";
            server.sendMessage(new TextMessageFromServer(answer), connectedClient);
        }*/
    }

    private String userXMLInfo(ConnectedClient connectedClient) {
        return "<user>\n" +
                "<name>" + connectedClient.getUsername() + "</name>\n" +
                "<type>" + connectedClient.getType() + "</type>\n" +
                "</user>\n";
    }
}
