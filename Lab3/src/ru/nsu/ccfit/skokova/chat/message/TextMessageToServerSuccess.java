package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class TextMessageToServerSuccess extends ServerMessage {
    public TextMessageToServerSuccess(String message) {
        super(message);
    }

    @Override
    public void interpret(Client client) {
        //If client gets this message, it needs to take message from its queue and show it
        try {
            Object message = client.getSentMessages().take();
            client.notifyValueChanged(message);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        }

    }
}
