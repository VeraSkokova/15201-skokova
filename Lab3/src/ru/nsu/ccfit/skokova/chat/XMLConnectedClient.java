package ru.nsu.ccfit.skokova.chat;

import java.net.Socket;
import java.util.Date;

public class XMLConnectedClient extends ConnectedClient {
    public XMLConnectedClient(Socket socket, Server server, String username) {
        this.socket = socket;
        this.server = server;
        this.username = username;
        this.type = "XML";
        this.date = new Date().toString() + "\n";
    }
}
