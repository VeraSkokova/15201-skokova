package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class ServerSuccessMessage extends ServerMessage {
    public ServerSuccessMessage(String message) {
        super(message);
    }

    @Override
    public void interpret(Client client) {}

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}