package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class TextMessageToServerSuccess extends ServerMessage {
    public TextMessageToServerSuccess(String message) {
        super(message);
    }

    @Override
    public void interpret(Client client) {}
}
