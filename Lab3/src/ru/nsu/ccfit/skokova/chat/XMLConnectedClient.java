package ru.nsu.ccfit.skokova.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class XMLConnectedClient extends ConnectedClient {
    public XMLConnectedClient(Socket socket, Server server, String username) {
        this.socket = socket;
        this.server = server;
        this.username = username;
        this.type = "XML";
        this.date = new Date().toString() + "\n";
    }

    public class Reader implements Runnable {
        @Override
        public void run() {
            try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
                ByteReader byteReader = new ByteReader(inputStream);
                while (!Thread.interrupted()) {
                    if (!isValid) {
                        break;
                    }
                    byte[] message = byteReader.readMessage();
                }
            } catch (IOException e) {
                logger.error("Can't read message :" + e.getMessage() + " I'm " + getSessionId());
            }
        }
    }

    public class Writer implements Runnable {
        @Override
        public void run() {

        }
    }
}
