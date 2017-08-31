package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.Server;

public class NewClientMessage extends ServerMessage {
    private static final Logger logger = LogManager.getLogger(Server.class);

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
