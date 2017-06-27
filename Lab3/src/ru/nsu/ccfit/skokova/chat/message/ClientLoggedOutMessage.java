package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class ClientLoggedOutMessage extends ServerMessage {
    private String username;

    public ClientLoggedOutMessage(String message) {
        super(message  + " logged out");
        this.username = message;
    }

    @Override
    public void interpret(Client client) {}

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getUsername() {
        return username;
    }
}
