package ru.nsu.ccfit.skokova.chat.message;

import ru.nsu.ccfit.skokova.chat.Client;

import java.util.ArrayList;

public class UserListSuccess extends ServerMessage {
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    public UserListSuccess(String message) {
        super(message);
    }

    @Override
    public void interpret(Client client) {}

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setUsernames(ArrayList<String> usernames) {
        this.usernames = usernames;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }
}
