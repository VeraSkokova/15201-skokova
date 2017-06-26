package ru.nsu.ccfit.skokova.chat;

import ru.nsu.ccfit.skokova.chat.gui.ClientFrame;
import ru.nsu.ccfit.skokova.chat.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ObjectStreamClient extends Client{
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    ObjectStreamClient() {}

    ObjectStreamClient(String server, int port, String username) {
        super(server, port, username);
    }

    public void start() {
        try {
            socket = new Socket(server, port);
        } catch(Exception ec) {
            display("Error in connection to server:" + ec.getMessage());
            logger.error("Error in connection to server:" + ec.getMessage());
        }

        String message = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(message);

        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            display("Exception creating new Input/output Streams: " + e.getMessage());
            logger.error("Exception creating new Input/output Streams: " + e.getMessage());
        }

        this.inThread = new Thread(new ObjectStreamClient.WriteToServer());
        this.outThread = new Thread(new ObjectStreamClient.ReadFromServer());

        this.inThread.start();
        this.outThread.start();

        /*try {
            outputStream.writeObject(username);
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }*/
    }

    private void display(String msg) {
        System.out.println(msg);
    }

    public void sendTextMessage(String message) {
        messages.add(new TextMessage(message));
        //sendMessage(new TextMessage(message));
    }

    public void sendLogoutMessage() {
        messages.add(new LogoutMessage());
        //sendMessage(new LogoutMessage());
    }

    public void sendUserListMessage() {
        messages.add(new UserListMessage());
        //sendMessage(new UserListMessage());
    }

    public void sendLoginMessage() {
        messages.add(new LoginMessage(this));
        //sendMessage(new LoginMessage(this));
    }

    private void sendMessage(ChatMessage msg) {
        try {
            outputStream.writeObject(msg);
        } catch(IOException e) {
            display("Exception writing to server: " + e.getMessage());
        }
    }

    private void disconnect() {
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null) {
                outputStream.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        // default values
        int portNumber = 1500;
        String serverAddress = "localhost";
        String userName = "Anonymous";

        Client client = new ObjectStreamClient(serverAddress, portNumber, userName);
        ClientFrame clientFrame = new ClientFrame(client);
        client.addHandler(clientFrame.new MessageUpdater());
    }

    class ReadFromServer implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    ChatMessage message = (ChatMessage) inputStream.readObject();
                    notifyValueChanged(message);
                    System.out.println(message.getMessage());
                    System.out.print("> ");
                } catch (IOException e) {
                    display("Server has closed the connection: " + e);
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    class WriteToServer implements Runnable {
        @Override
        public void run() {
            try {
                Scanner scan = new Scanner(System.in);
                while (true) {
                    System.out.print("> ");
                    /*String msg = scan.nextLine();
                    if (msg.equalsIgnoreCase("LOGOUT")) {
                        sendLogoutMessage();
                        break;
                    } else if (msg.equalsIgnoreCase("WHOISIN")) {
                        sendUserListMessage();
                    } else {
                        sendTextMessage(msg);
                    }*/
                    sendMessage(messages.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
