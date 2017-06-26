package ru.nsu.ccfit.skokova.chat;

public class ClientPair {
    private String name;
    private String type;

    public ClientPair(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
