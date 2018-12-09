package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.chat.message.Message;
import ru.nsu.ccfit.skokova.chat.message.TextMessageToServerError;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectStreamConnectedClient extends ConnectedClient {
    private static final Logger logger = LogManager.getLogger(Server.class);

    public ObjectStreamConnectedClient(Socket socket, Server server) {
        super(socket, server);
        this.type = "ObjectStream";
        this.readerThread = new Thread(new Reader());
        this.writerThread = new Thread(new Writer());
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

    public class Reader implements Runnable {
        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                while(!Thread.interrupted()) {
                    if (!isValid) {
                        break;
                    }
                    Message message = (Message) inputStream.readObject();
                    message.setUsername(ObjectStreamConnectedClient.this.getUsername());
                    message.process(server, ObjectStreamConnectedClient.this);
                }
            } catch (IOException e) {
                logger.error("Can't read message (IOException)");
                close();
                interrupt();
            } catch (ClassNotFoundException | ClassCastException e) {
                logger.error("Unknown type of message. " + e.getMessage());
            } catch (NullPointerException e) {
                logger.error("Invalid type of message: " + e.getCause());
            } catch (OutOfMemoryError e) {
                logger.error("Big message");
                messages.add(new TextMessageToServerError("Message is too big"));
            }
        }
    }

    public class Writer implements Runnable {
        @Override
        public void run() {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
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
