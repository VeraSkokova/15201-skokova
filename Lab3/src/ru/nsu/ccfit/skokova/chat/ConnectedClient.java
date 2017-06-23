package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.message.ChatMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectedClient {
    private static final int MAX_MSG_COUNT = 10000;
    private int sessionId;
    private Socket socket;
    private Server server;

    private String username;
    private String date;
    private boolean isValid;

    private static final Logger logger = LogManager.getLogger(ConnectedClient.class);

    private BlockingQueue<ChatMessage> messages = new ArrayBlockingQueue<>(MAX_MSG_COUNT);

    private Thread readerThread;
    private Thread writerThread;

    public ConnectedClient(Socket socket, Server server, String username) {
        this.socket = socket;
        this.server = server;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public BlockingQueue<ChatMessage> getMessages() {
        return messages;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void login(Server server) {
        this.setSessionId(server.setUserSessionId());
        server.addClient(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isValid() {
        return isValid;
    }

    public class Reader implements Runnable {
        @Override
        public void run() {
            try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                while(!Thread.interrupted()) {
                    if (!isValid) {
                        break;
                    }
                    ChatMessage message = (ChatMessage) inputStream.readObject();
                    message.process(server, ConnectedClient.this);
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Reader was interrupted");
                }
            } catch (IOException e) {
                logger.error("Can't read message :" + e.getMessage() + " I'm " + getSessionId());
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            } catch (ClassCastException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
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
                    ChatMessage message = messages.take(); //TODO ?
                    outputStream.writeObject(message);
                }
            } catch (IOException e) {
                logger.error("Can't create an outputStream" + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Writer was interrupted");
            }
        }
    }
}
