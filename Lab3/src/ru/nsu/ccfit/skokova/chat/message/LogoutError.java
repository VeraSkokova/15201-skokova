package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LogoutError extends ServerMessage {
    public LogoutError(String reason) {
        super(reason);
    }

    @Override
    public void interpret(Client client) {
        client.notifyValueChanged(this);
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
