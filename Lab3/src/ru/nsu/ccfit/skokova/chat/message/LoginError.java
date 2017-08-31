package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LoginError extends ServerMessage {
    public LoginError(String reason) {
        super(reason);
    }

    @Override
    public void interpret(Client client) {
        try {
            Message loginMessage = client.getSentMessages().take();
            client.notifyValueChanged(this);
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
