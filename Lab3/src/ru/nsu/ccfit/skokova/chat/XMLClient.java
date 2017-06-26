package ru.nsu.ccfit.skokova.chat;

import ru.nsu.ccfit.skokova.chat.message.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class XMLClient extends Client {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private XMLMessageCreator xmlMessageCreator = new XMLMessageCreator();

    XMLClient() {
    }

    XMLClient(String server, int port, String username) {
        super(server, port, username);
    }

    public void start() {
        try {
            socket = new Socket(server, port);
        } catch (Exception ec) {
            logger.error("Error in connection to server:" + ec.getMessage());
        }

        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Exception creating new Input/output Streams: " + e.getMessage());
        }

        this.inThread = new Thread(new XMLClient.ReadFromServer());
        this.outThread = new Thread(new XMLClient.WriteToSever());

        this.inThread.start();
        this.outThread.start();

        //TODO
    }

    private void display(String msg) {
        System.out.println(msg);
    }

    public void sendTextMessage(String message) {
        sendMessage(xmlMessageCreator.createClientMessage(message, "1")); //!!!!!!!!!!
    }

    public void sendLogoutMessage() {
        sendMessage(xmlMessageCreator.createLogoutMessage(this.username));
    }

    public void sendUserListMessage() {
        sendMessage(xmlMessageCreator.createUserListRequestMessage("1")); //!!!!!!!!!!!!
    }

    private void sendMessage(String msg) {
        try {
            outputStream.writeInt(msg.getBytes().length);
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch(IOException e) {
            display("Exception writing to server: " + e.getMessage());
        }
    }

    //TODO
    class ReadFromServer implements Runnable {
        @Override
        public void run() {
            ByteReader byteReader = new ByteReader(inputStream);
            while (true) {
                byte[] messageBytes = byteReader.readMessage();
                if (messageBytes == null) {
                    logger.error("Message hasn't been read");
                } else {
                    String msg = new String(messageBytes);
                    //TODO : parse or something else
                }
            }
        }
    }

    class WriteToSever implements Runnable {
        @Override
        public void run() {
            Scanner scan = new Scanner(System.in);
            try {
                while (true) {
                    String message = xmlMessages.take();
                    sendMessage(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

