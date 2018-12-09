package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class NewClientMessage extends ServerMessage {
    private String type;

    public NewClientMessage(String message, String type) {
        this.message = message;
        this.type = type;
    }

    @Override
    public void interpret(Client client) {
        client.notifyValueChanged(this);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.message + " logged in";
    }
}
