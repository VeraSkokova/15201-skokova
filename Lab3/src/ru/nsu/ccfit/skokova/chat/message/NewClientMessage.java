package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class NewClientMessage extends ServerMessage {
    public NewClientMessage(String message) {
        this.message = message;
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
        return this.message + " logged in";
    }
}
