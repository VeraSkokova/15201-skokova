package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.skokova.chat.message.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class XMLConnectedClient extends ConnectedClient {
    private static final Logger logger = LogManager.getLogger(Server.class);

    public XMLConnectedClient(Socket socket, Server server) {
        super(socket, server);
        this.type = "XML";
        this.readerThread = new Thread(new XMLConnectedClient.Reader());
        this.writerThread = new Thread(new XMLConnectedClient.Writer());
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

    public void setUsername(String username) {
        super.setUsername(username);
    }

    public class Reader implements Runnable {
        @Override
        public void run() {
            XMLToMessage xmlToMessage = new XMLToMessage();
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                ByteReader byteReader = new ByteReader(inputStream);
                while (!Thread.interrupted()) {
                    if (!isValid) {
                        break;
                    }
                    byte[] message = byteReader.readMessage();
                    String msg = new String(message, StandardCharsets.UTF_8);
                    ChatMessage chatMessage = xmlToMessage.parseMessage(new InputSource(new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8))));
                    chatMessage.setUsername(XMLConnectedClient.this.getUsername());
                    chatMessage.setSessionId(sessionId);
                    chatMessage.process(server, XMLConnectedClient.this);
                }
            } catch (IOException e) {
                logger.error("Can't read message :" + e.getMessage());
                close();
                interrupt();
            } catch (ParserConfigurationException | SAXException e) {
                logger.error("Can't parse message :" + e.getMessage());
            } catch (OutOfMemoryError e) {
                logger.error("Big message");
                messages.add(new TextMessageToServerError("Message is too big"));
            }
        }
    }

    public class Writer implements Runnable {
        MessageToXML messageToXML = new MessageToXML();
        @Override
        public void run() {
            try {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                while (!Thread.interrupted()) {
                    Message message = messages.take();
                    logger.debug("Writing " + message.getClass() + " " + message.toString() + " to " + getUsername());
                    String msg = messageToXML.parseMessage((ServerMessage) message);
                    outputStream.writeInt(msg.getBytes(StandardCharsets.UTF_8).length);
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
            } catch (IOException e) {
                logger.info("Can't create an outputStream: " + e.getMessage());
                //interrupt();
                //close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Writer was interrupted");
            }
        }
    }
}
