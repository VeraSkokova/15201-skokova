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
    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65535;
    private static final int MESSAGES_COUNT = 10000;
    private static final AtomicInteger SESSION_ID = new AtomicInteger(1);

    private static final Logger logger = LogManager.getLogger(Server.class);

    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    private ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private BlockingQueue<Message> messageHistory = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<ChatMessage> requests = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private BlockingQueue<String> usernames = new ArrayBlockingQueue<>(MESSAGES_COUNT);
    private int firstPort;
    private int secondPort;
    private boolean isWorking;
    private Thread objectThread;
    private Thread xmlThread;

    public Server(int firstPort, int secondPort) {
        this.firstPort = firstPort;
        this.secondPort = secondPort;
    }

    public Server(ConfigParser configParser) {
        this.firstPort = ConfigParser.map.get("ObjectPort");
        this.secondPort = ConfigParser.map.get("XMLPort");
    }

    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            ConfigParser configParser = new ConfigParser("/home/veraskokova/IdeaProjects/Chat/src/MyConfig.txt");
        /*int portNumber = 4500;
        int anotherPortNumber = 1700;*/
            Server server = new Server(configParser);
            server.start();
            while (bufferedReader.readLine().compareTo("stop") != 0) ;
            server.stop();
        } catch (IOException | BadParseException e) {
            logger.warn(e.getMessage());
        }
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
        System.exit(0);
    }

    public void display(String msg) {
        logger.info(msg);
    }

    public synchronized void broadcast(Message message) {
        for (ConnectedClient connectedClient : connectedClients) {
            if ((connectedClient.isValid()) && (!connectedClient.getUsername().equals(message.getUsername()))) {
                logger.debug("Sending " + message.getMessage() + " from " + message.getUsername() + " to" + connectedClient.getUsername());
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