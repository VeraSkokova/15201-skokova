package ru.nsu.ccfit.skokova.chat;

import ru.nsu.ccfit.skokova.chat.message.ChatMessage;
import ru.nsu.ccfit.skokova.chat.message.LogoutMessage;
import ru.nsu.ccfit.skokova.chat.message.TextMessage;
import ru.nsu.ccfit.skokova.chat.message.UserListMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client  {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;

    private String server;
    private String username;
    private int port;

    private Thread inThread;
    private Thread outThread;

    Client(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    public boolean start() {
        try {
            socket = new Socket(server, port);
        } catch(Exception ec) {
            display("Error in connection to server:" + ec);
            return false;
        }

        String message = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(message);

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        this.inThread = new Thread(new WriteToServer());
        this.outThread = new Thread(new ReadFromServer());

        this.inThread.start();
        this.outThread.start();

        /*try {
            outputStream.writeObject(username);
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }*/
        return true;
    }

    private void display(String msg) {
        System.out.println(msg);
    }

    private void sendMessage(ChatMessage msg) {
        try {
            outputStream.writeObject(msg);
        } catch(IOException e) {
            display("Exception writing to server: " + e);
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

    public static void main(String[] args) {
        // default values
        int portNumber = 1500;
        String serverAddress = "localhost";
        String userName = "Anonymous";

        switch(args.length) {
            case 3:
                serverAddress = args[2];
            case 2:
                try {
                    portNumber = Integer.parseInt(args[1]);
                }
                catch(Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
                    return;
                }
            case 1:
                userName = args[0];
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
                return;
        }
        Client client = new Client(serverAddress, portNumber, userName);
        if(!client.start())
            return;
    }

    class ReadFromServer implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    ChatMessage message = (ChatMessage) inputStream.readObject();
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
            Scanner scan = new Scanner(System.in);
            while(true) {
                System.out.print("> ");
                String msg = scan.nextLine();
                if(msg.equalsIgnoreCase("LOGOUT")) {
                    sendMessage(new LogoutMessage());
                    break;
                }
                else if(msg.equalsIgnoreCase("WHOISIN")) {
                    sendMessage(new UserListMessage());
                }
                else {
                    sendMessage(new TextMessage(msg));
                }
            }
        }
    }
}
