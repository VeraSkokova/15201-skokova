package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class ServerErrorMessage extends ServerMessage {
    public ServerErrorMessage(String message) {
        super(message);
    }

    @Override
    public void interpret(Client client) {
        try {
            Message temp = client.getSentMessages().take();
            /*if (temp instanceof LogoutMessage) {
                LogoutError logoutError = new LogoutError(message);
                logoutError.interpret(client);
            } else  if (temp instanceof  LoginMessage) {
                LoginError loginError = new LoginError(message);
                loginError.interpret(client);
            } else {
                TextMessageToServerError textMessageToServerError = new TextMessageToServerError(message);
                textMessageToServerError.interpret(client);
            }*/
            client.notifyValueChanged(this);
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
        return message;
    }
}
