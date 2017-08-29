package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LoginError extends ServerMessage {
    public LoginError(String reason) {
        super(reason);
    }

    @Override
    public void interpret(Client client) {
        try {
            Object loginMessage = client.getSentMessages().take();
        } catch (InterruptedException e) {
            logger.debug(e.getMessage());
        }
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return message;
    }
}
