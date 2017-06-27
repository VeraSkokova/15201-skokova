package ru.nsu.ccfit.skokova.chat;

import ru.nsu.ccfit.skokova.chat.message.ChatMessage;
import ru.nsu.ccfit.skokova.chat.message.LoginMessage;
import ru.nsu.ccfit.skokova.chat.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class ObjectStreamConnectedClient extends ConnectedClient{
    public ObjectStreamConnectedClient(Socket socket, Server server, String username) {
        super(socket, server);
        this.username = username;
        this.type = "ObjectStream";
        this.readerThread = new Thread(new Reader());
        this.writerThread = new Thread(new Writer());
        this.date = new Date().toString() + "\n";
    }

    public ObjectStreamConnectedClient(Socket socket, Server server) {
        super(socket, server);
        this.type = "ObjectStream";
        this.readerThread = new Thread(new Reader());
        this.writerThread = new Thread(new Writer());
        this.date = new Date().toString() + "\n";
    }

    public void run() {
        this.isValid = true;
        readerThread.start();
        writerThread.start();
        logger.info("New connected client");
    }

    public void interrupt() {
        this.isValid = false;
        readerThread.interrupt();
        writerThread.interrupt();
        logger.info("Connected client stopped working");
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            logger.error("Can't close socket");
        }
    }

    public void login(Server server) {
        this.setSessionId(server.setUserSessionId());
    }

    public Socket getSocket() {
        return socket;
    }

    public class Reader implements Runnable {
        @Override
        public void run() {
            try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                while(!Thread.interrupted()) {
                    if (!isValid) {
                        break;
                    }
                    Message message = (Message) inputStream.readObject();
                    message.setConnectedClient(ObjectStreamConnectedClient.this);
                    message.process(server);
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Reader was interrupted");
                }
            } catch (IOException e) {
                logger.error("Can't read message :" + e.getMessage() + " I'm " + getSessionId());
            } catch (ClassNotFoundException | ClassCastException e) {
                logger.error("Unknown type of message. " + e.getMessage());
            } catch (NullPointerException e) {
                logger.error("Invalid type of message");
            }
        }
    }

    public class Writer implements Runnable {
        @Override
        public void run() {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
                while(!Thread.interrupted()) {
                    Message message = messages.take();
                    outputStream.writeObject(message);
                }
            } catch (IOException e) {
                logger.info("Can't create an outputStream" + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Writer was interrupted");
            }
        }
    }
}
