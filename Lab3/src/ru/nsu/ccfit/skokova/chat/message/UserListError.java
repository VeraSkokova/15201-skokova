package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class UserListError extends ServerMessage {
    public UserListError(String reason) {
        super(reason);
    }

    @Override
    public void interpret(Client client) {
        try {
            Object userListMessage = client.getSentMessages().take();
            client.notifyValueChanged(this);
        } catch (InterruptedException e) {
            logger.debug(e.getMessage());
        }
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
