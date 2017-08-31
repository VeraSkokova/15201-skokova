package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.Server;

public class TextMessageToServerSuccess extends ServerMessage {
    private static final Logger logger = LogManager.getLogger(Server.class);

    public TextMessageToServerSuccess(String message) {
        super(message);
    }

    @Override
    public void interpret(Client client) {
        try {
            Object message = client.getSentMessages().take();
            logger.debug("Took msg " + message);

            if (message != null) {
                client.notifyValueChanged(message);
            }
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        }

    }
}
