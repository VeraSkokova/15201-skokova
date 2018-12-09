package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class TextMessageFromServer extends ServerMessage {
    public TextMessageFromServer(String message, String sender) {
        this.message = message;
        this.username = sender;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void interpret(Client client) {
        client.notifyValueChanged(this);
    }

    @Override
    public String toString() {
        return username + ": " + message;
    }
}
