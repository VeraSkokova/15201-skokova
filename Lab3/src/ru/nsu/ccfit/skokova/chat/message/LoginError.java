package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LoginError extends ServerMessage {
    public LoginError(String reason) {
        super(reason);
    }

    @Override
    public void interpret(Client client) {}

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
