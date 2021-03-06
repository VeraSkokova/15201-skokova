package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.skokova.chat.message.ChatMessage;
import ru.nsu.ccfit.skokova.chat.message.Message;

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

    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65535;
    private static final int MESSAGES_COUNT = 10000;
    private static final AtomicInteger SESSION_ID = new AtomicInteger(1);
    private static final Logger logger = LogManager.getLogger(Server.class);

    private ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private BlockingQueue<Message> messageHistory = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<ChatMessage> requests = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<String> usernames = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private int firstPort;
    private int secondPort;
    private boolean isWorking;
    private Thread objectThread;
    private Thread xmlThread;

    private final Object lock = new Object();

    public Server(int firstPort, int secondPort) {
        this.firstPort = firstPort;
        this.secondPort = secondPort;
    }

    public Server(ConfigParser configParser) {
        this.firstPort = ConfigParser.map.get("ObjectPort");
        this.secondPort = ConfigParser.map.get("XMLPort");
        if (!configParser.isEnableLogs()) {
            Logger logger = LogManager.getRootLogger();
            Configurator.setLevel(logger.getName(), Level.OFF);
        }
    }

    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            ConfigParser configParser = new ConfigParser("src/MyConfig.txt");
            Server server = new Server(configParser);
            server.start();
            while (bufferedReader.readLine().compareTo("stop") != 0) ;
            server.stop();
        } catch (IOException | BadParseException e) {
            logger.warn(e.getMessage());
        }
    }

    public ArrayList<ConnectedClient> getConnectedClients() {
        synchronized (lock) {
            return connectedClients;
        }
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
        synchronized (lock) {
            for (ConnectedClient connectedClient : connectedClients) {
                connectedClient.interrupt();
                connectedClient.close();
            }
        }
        objectThread.interrupt();
        xmlThread.interrupt();
        System.exit(0);
    }

    public void display(String msg) {
        logger.info(msg);
    }

    public void broadcast(Message message) {
        synchronized (lock) {
            for (ConnectedClient connectedClient : connectedClients) {
                if ((connectedClient.isValid()) && (!connectedClient.getUsername().equals(message.getUsername()))) {
                    logger.debug("Sending " + message.getMessage() + " from " + message.getUsername() + " to " + connectedClient.getUsername());
                    sendMessage(message, connectedClient);
                }
            }
        }
    }

    public void sendMessage(Message message, ConnectedClient connectedClient) {
        connectedClient.getMessages().add(message);
    }

    public void saveMessage(Message message) {
        messageHistory.add(message);
    }

    public void removeClient(ConnectedClient connectedClient) {
        synchronized (lock) {
            connectedClients.remove(connectedClient);
        }
    }

    public int setUserSessionId() {
        return SESSION_ID.getAndIncrement();
    }

    public void addClient(ConnectedClient connectedClient) {
        synchronized (lock) {
            this.connectedClients.add(connectedClient);
        }
    }

    public void setFirstPort(int firstPort) {
        this.firstPort = firstPort;
    }

    public void setSecondPort(int secondPort) {
        this.secondPort = secondPort;
    }

    public void sendMessageHistory(ConnectedClient connectedClient) {
        for (Message message : messageHistory) {
            sendMessage(message, connectedClient);
        }
    }

    public void connectObjectStreamClient(Socket socket) {
        ObjectStreamConnectedClient connectedClient = new ObjectStreamConnectedClient(socket, this);
        connectedClient.run();
        connectedClient.login(this);
    }

    public void connectXMLClient(Socket socket) {
        XMLConnectedClient connectedClient = new XMLConnectedClient(socket, this);
        connectedClient.run();
        connectedClient.login(this);
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
                    Thread.currentThread().interrupt();
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
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}