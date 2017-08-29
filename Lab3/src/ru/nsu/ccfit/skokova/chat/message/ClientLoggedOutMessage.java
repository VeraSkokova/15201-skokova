package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class ClientLoggedOutMessage extends ServerMessage {
    private String username;

    public ClientLoggedOutMessage(String username) {
        this.username = username;
    }

    @Override
    public void interpret(Client client) {
        client.notifyValueChanged(this);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username + " logged out";
    }
}
