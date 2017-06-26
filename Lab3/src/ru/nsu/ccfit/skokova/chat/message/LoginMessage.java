package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

public class LoginMessage extends ChatMessage{
    private String username;

    public LoginMessage(Client client) {
        this.username = client.getUsername();
    }
}
