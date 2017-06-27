package ru.nsu.ccfit.skokova.chat;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.skokova.chat.message.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class XMLConnectedClient extends ConnectedClient {
    private DocumentBuilder documentBuilder;

    public XMLConnectedClient(Socket socket, Server server, String username) {
        super(socket, server);
        this.username = username;
        this.type = "XML";
        this.readerThread = new Thread(new XMLConnectedClient.Reader());
        this.writerThread = new Thread(new XMLConnectedClient.Writer());
        this.date = new Date().toString() + "\n";
    }

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
        XMLToMessage xmlToMessage = new XMLToMessage();
        @Override
        public void run() {
            logger.debug("Sending message to Server");
            try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
                ByteReader byteReader = new ByteReader(inputStream);
                documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                while (!Thread.interrupted()) {
                    if (!isValid) {
                        break;
                    }
                    byte[] message = byteReader.readMessage();
                    String msg = new String(message);
                    logger.debug("Received " + msg);
                    Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8))));
                    ChatMessage chatMessage = xmlToMessage.parseMessage(document);
                    chatMessage.setConnectedClient(XMLConnectedClient.this);
                    chatMessage.setSessionId(sessionId);
                    chatMessage.process(server);
                }
            } catch (IOException e) {
                logger.error("Can't read message :" + e.getMessage() + " I'm " + getSessionId());
                close();
                interrupt();
            } catch (ParserConfigurationException | SAXException e) {
                logger.error("Can't parse message :" + e.getMessage());
            }
        }
    }

    public class Writer implements Runnable {
        MessageToXML messageToXML = new MessageToXML();
        @Override
        public void run() {
            try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                while (!Thread.interrupted()) {
                    Message message = messages.take();
                    logger.debug("Message: " + message + " " + message.getMessage());
                    message.setConnectedClient(XMLConnectedClient.this);

                    String msg = messageToXML.parseMessage((ServerMessage) message);
                    outputStream.writeInt(msg.length());
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
            } catch (IOException e) {
                logger.info("Can't create an outputStream, " + e.getMessage());
                if (server.getConnectedClients().contains(XMLConnectedClient.this)) {
                    server.removeClient(XMLConnectedClient.this);
                }
                interrupt();
                close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Writer was interrupted");
            }
        }
    }
}
