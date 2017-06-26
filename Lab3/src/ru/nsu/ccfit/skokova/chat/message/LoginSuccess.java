package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LoginSuccess extends ServerMessage {
    private int sessionId;

    public LoginSuccess(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void interpret(Client client) {
        client.setLoggedIn(true);
        client.setSessionId(this.sessionId);
        logger.debug("nc :" + client.getSessionId());
        setMessage("Welcome!");
    }
}