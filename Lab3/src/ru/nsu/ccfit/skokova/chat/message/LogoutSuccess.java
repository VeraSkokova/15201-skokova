package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LogoutSuccess extends ServerMessage {
    @Override
    public void interpret(Client client) {
        setMessage("Bye!");
        client.setLoggedIn(false);
        client.interrupt();
        client.disconnect();
    }
}
