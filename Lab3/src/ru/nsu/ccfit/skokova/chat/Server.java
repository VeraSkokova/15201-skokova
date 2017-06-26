package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.message.ChatMessage;
import ru.nsu.ccfit.skokova.chat.message.TextMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int MESSAGES_COUNT = 10000;
    private static final AtomicInteger SESSION_ID = new AtomicInteger(1);
    private ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private BlockingQueue<ChatMessage> messageHistory = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<ChatMessage> requests = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private SimpleDateFormat sdf;
    private int port;
    private boolean isWorking;

    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65535;

    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server(int port) {
        this.port = port;
        this.sdf = new SimpleDateFormat("HH:mm:ss");
    }

    public ArrayList<ConnectedClient> getConnectedClients() {
        return connectedClients;
    }

    public BlockingQueue<ChatMessage> getRequests() {
        return requests;
    }

    public void start() {
        isWorking = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (isWorking) {
                display("Server waiting for Clients on port " + port + ".");

                Socket socket = serverSocket.accept();
                if (!isWorking)
                    break;

                connectClient(socket);
            }

            try {
                serverSocket.close();
                for (ConnectedClient connectedClient : connectedClients) {
                    connectedClient.interrupt();
                    connectedClient.close();
                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

    protected void stop() {
        isWorking = false;
        try {
            new Socket("localhost", port);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
        logger.info(time);
    }

    public synchronized void broadcast(ChatMessage message) {
        String time = sdf.format(new Date());
        String messageLf = time + " " + message.getMessage() + "\n";
        logger.info(messageLf);

        for (ConnectedClient connectedClient : connectedClients) {
            if (connectedClient.isValid()) {
                sendMessage(message, connectedClient);
            }
        }
    }

    public void sendMessage(ChatMessage message, ConnectedClient connectedClient) {
        connectedClient.getMessages().add(message);
    }

    public void saveMessage(ChatMessage message) {
        messageHistory.add(message);
    }

    public synchronized void removeClient(ObjectStreamConnectedClient connectedClient) {
        connectedClients.remove(connectedClient);
    }

    public int setUserSessionId() {
        return SESSION_ID.getAndIncrement();
    }

    public synchronized void addClient(ObjectStreamConnectedClient connectedClient) {
        this.connectedClients.add(connectedClient);
    }

    public void check(TextMessage message) {

    }

    private void connectClient(Socket socket) {
        ObjectStreamConnectedClient connectedClient = new ObjectStreamConnectedClient(socket, this, "username");
        connectedClient.run();
        connectedClient.login(this);
        broadcast(new TextMessage("New user logged in"));
        for (ChatMessage message : messageHistory) {
            sendMessage(message, connectedClient);
        }
    }

    public static void main(String[] args) {
        int portNumber = 1500;
        switch (args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Server [portNumber]");
                return;

        }
        Server server = new Server(portNumber);
        server.start();
    }
}