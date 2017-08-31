package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class ServerSuccessMessage extends ServerMessage {
    public ServerSuccessMessage() {
    }

    @Override
    public void interpret(Client client) {
        try {
            Message temp = client.getSentMessages().take();
            if (temp instanceof LogoutMessage) {
                client.setLoggedIn(false);
                client.interrupt();
                client.disconnect();
                client.getClientConnectedHandler().handle(false);
            } else {
                client.notifyValueChanged(temp);
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return "";
    }
}
