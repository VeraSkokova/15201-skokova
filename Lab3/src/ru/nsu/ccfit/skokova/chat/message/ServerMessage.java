package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.ConnectedClient;

public abstract class ServerMessage extends Message {
    protected String message;

    public ServerMessage() {}

    public ServerMessage(String message) {
        this.message = message;
    }

    public ServerMessage(ConnectedClient connectedClient) {
        super(connectedClient);
    }

    public abstract void interpret(Client client);

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
