package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.message.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    private ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private SimpleDateFormat sdf;
    private int port;
    private boolean isWorking;

    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server(int port) {
        this.port = port;
        this.sdf = new SimpleDateFormat("HH:mm:ss");
    }

    public ArrayList<ConnectedClient> getConnectedClients() {
        return connectedClients;
    }

    public void start() {
        isWorking = true;
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);

            while(isWorking)
            {
                display("Server waiting for Clients on port " + port + ".");

                Socket socket = serverSocket.accept();
                if(!isWorking)
                    break;

                ConnectedClient connectedClient = new ConnectedClient(socket, this, "username");
                connectedClients.add(connectedClient);
                connectedClient.run();
            }

            try {
                serverSocket.close();
                for (ConnectedClient tc : connectedClients) {
                    tc.interrupt();
                    tc.close();
                }
            }
            catch(Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        }
        catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }
    protected void stop() {
        isWorking = false;
        try {
            new Socket("localhost", port);
        }
        catch(IOException e) {
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

        for(int i = connectedClients.size(); --i >= 0;) {
            ConnectedClient ct = connectedClients.get(i);
            try {
                ct.getMessages().put(message);
            } catch (InterruptedException e) {
                logger.warn("Interrupted while sending a message");
            }
        }
    }

    public boolean sendMessage(ChatMessage message, ConnectedClient connectedClient) {
        try {
            connectedClient.getMessages().put(message);
        } catch(InterruptedException e) {
            logger.error("Can't send message to client." + e.getMessage());
            return false;
        }
        return true;
    }

    public synchronized void removeClient(ConnectedClient connectedClient) {
        connectedClients.remove(connectedClient);
    }

    public static void main(String[] args) {
        int portNumber = 1500;
        switch(args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                }
                catch(Exception e) {
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