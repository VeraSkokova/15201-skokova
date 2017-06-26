package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.gui.ValueChangedHandler;
import ru.nsu.ccfit.skokova.chat.message.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Client  {
    private static final int MESSAGES_COUNT = 10000;
    protected Socket socket;

    protected String server;
    protected String username;
    protected int port;

    protected Thread inThread;
    protected Thread outThread;

    protected BlockingQueue<ChatMessage> messages = new ArrayBlockingQueue<ChatMessage>(MESSAGES_COUNT);
    protected ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

    protected Logger logger = LogManager.getLogger(Client.class);

    Client() {}

    Client(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }


    public void addHandler(ValueChangedHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
    }

    public void notifyValueChanged(Object value) {
        for (ValueChangedHandler handler : handlers) {
            handler.handle(value);
        }
    }


    public void start() {}

    /*private void display(String msg) {
        System.out.println(msg);
    }*/

    public void sendTextMessage(String message) {}

    public void sendLogoutMessage() {}

    public void sendUserListMessage() {}

    private void disconnect() {}

    public void setServer(String server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }
}
