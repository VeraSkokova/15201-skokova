package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.message.ChatMessage;
import ru.nsu.ccfit.skokova.chat.message.Message;
import ru.nsu.ccfit.skokova.chat.message.TextMessageFromServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    private static final int MESSAGES_COUNT = 10000;
    private static final AtomicInteger SESSION_ID = new AtomicInteger(1);
    private ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private BlockingQueue<Message> messageHistory = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<ChatMessage> requests = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<String> usernames = new ArrayBlockingQueue<String>(MESSAGES_COUNT);
    private int firstPort;
    private int secondPort;
    private boolean isWorking;

    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65535;

    private Thread objectThread;
    private Thread xmlThread;

    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server(int firstPort, int secondPort) {
        this.firstPort = firstPort;
        this.secondPort = secondPort;
    }

    public ArrayList<ConnectedClient> getConnectedClients() {
        return connectedClients;
    }

    public BlockingQueue<ChatMessage> getRequests() {
        return requests;
    }

    public BlockingQueue<String> getUsernames() {
        return usernames;
    }

    public void start() {
        isWorking = true;
        objectThread = new Thread(new ObjectStreamAcceptor());
        xmlThread = new Thread(new XMLAcceptor());
        objectThread.start();
        xmlThread.start();
    }

    protected void stop() {
        isWorking = false;
        for (ConnectedClient connectedClient : connectedClients) {
            connectedClient.interrupt();
            connectedClient.close();
        }
        objectThread.interrupt();
        xmlThread.interrupt();
    }

    public void display(String msg) {
        logger.info(msg);
    }

    public synchronized void broadcast(Message message) {
        for (ConnectedClient connectedClient : connectedClients) {
            if (connectedClient.isValid()) {
                sendMessage(message, connectedClient);
            }
        }
    }

    public void sendMessage(ChatMessage message, ConnectedClient connectedClient) {
        connectedClient.getMessages().add(message);
    }

    public void sendMessage(Message message, ConnectedClient connectedClient) {
        connectedClient.getMessages().add(message);
    }

    public void saveMessage(Message message) {
        messageHistory.add(message);
    }

    public synchronized void removeClient(ConnectedClient connectedClient) {
        connectedClients.remove(connectedClient);
    }

    public int setUserSessionId() {
        return SESSION_ID.getAndIncrement();
    }

    public synchronized void addClient(ConnectedClient connectedClient) {
        this.connectedClients.add(connectedClient);
    }

    public void setFirstPort(int firstPort) {
        this.firstPort = firstPort;
    }

    public void setSecondPort(int secondPort) {
        this.secondPort = secondPort;
    }

    public void connectObjectStreamClient(Socket socket) {
        ObjectStreamConnectedClient connectedClient = new ObjectStreamConnectedClient(socket, this);
        connectedClient.run();
        connectedClient.login(this);
        broadcast(new TextMessageFromServer("New user logged in", "Server"));
        for (Message message : messageHistory) {
            sendMessage(message, connectedClient);
        }
    }

    public void connectXMLClient(Socket socket) {
        XMLConnectedClient connectedClient = new XMLConnectedClient(socket, this);
        connectedClient.run();
        connectedClient.login(this);
        broadcast(new TextMessageFromServer("New user logged in", "Server"));
        for (Message message : messageHistory) {
            sendMessage(message, connectedClient);
        }
    }

    public static void main(String[] args) {
        int portNumber = 4500;
        int anotherPortNumber = 1700;
        Server server = new Server(portNumber, anotherPortNumber);
        server.start();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            if (bufferedReader.readLine().equals("stop")) {
                server.stop();
            }
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    class ObjectStreamAcceptor implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    ServerSocket serverSocket = new ServerSocket(firstPort);
                    while (isWorking) {
                        display("Server waiting for ObjectStream Clients on firstPort " + firstPort + ".");
                        Socket socket = serverSocket.accept();
                        if (!isWorking)
                            break;
                        connectObjectStreamClient(socket);
                    }
                } catch (IOException e) {
                    logger.error(" Exception on new ServerSocket: " + e.getMessage());
                }
            }
        }
    }

    class XMLAcceptor implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    ServerSocket serverSocket = new ServerSocket(secondPort);
                    while (isWorking) {
                        display("Server waiting for XMLClients on secondPort " + secondPort + ".");
                        Socket socket = serverSocket.accept();
                        if (!isWorking)
                            break;
                        connectXMLClient(socket);
                    }
                } catch (IOException e) {
                    logger.error(" Exception on new ServerSocket: " + e.getMessage());
                }
            }
        }
    }
}