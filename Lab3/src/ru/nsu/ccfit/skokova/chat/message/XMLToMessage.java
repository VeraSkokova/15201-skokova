package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.nsu.ccfit.skokova.chat.ClientPair;
import ru.nsu.ccfit.skokova.chat.MessagePair;
import ru.nsu.ccfit.skokova.chat.Server;

public class XMLToMessage {
    private static final Logger logger = LogManager.getLogger(XMLToMessage.class);

    public ChatMessage parseMessage(Document document) {
        logger.debug("Started conversion");
        ChatMessage message = null;
        Node root = document.getDocumentElement();
        Element temp = (Element)root;
        logger.debug(root.getNodeName());
        logger.debug(temp.getAttribute("name"));
        switch (root.getNodeName()) {
            case "command":
                switch (temp.getAttribute("name")) {
                    case "list":
                        int sessionID = parseSimpleCommand(temp);
                        message = new UserListMessage(sessionID);
                        break;
                    case "login":
                        ClientPair clientPair = parseLogincommand(temp);
                        message = new LoginMessage(clientPair.getName(), clientPair.getType());
                        break;
                    case "logout":
                       int sessionId = parseSimpleCommand(temp);
                       message = new LogoutMessage(sessionId);
                       break;
                    case "message":
                        MessagePair messagePair = parseMessageCommand(temp);
                        message = new TextMessageToServer(messagePair.getMessage(), messagePair.getSessionID());
                        break;
                }
                break;
            default:
                break;
        }
        logger.debug("After convertion: " + message + " " + message.getMessage());
        return message;
    }

    private int parseSimpleCommand(Element element) {
        String sessionID = element.getElementsByTagName("session").item(0).getTextContent();
        int result = Integer.parseInt(sessionID);
        return result;
    }

    private ClientPair parseLogincommand(Element element) {
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        String type = element.getElementsByTagName("type").item(0).getTextContent();
        return new ClientPair(name, type);
    }

    private MessagePair parseMessageCommand(Element element) {
        String message = element.getElementsByTagName("message").item(0).getTextContent();
        String sessionID = element.getElementsByTagName("session").item(0).getTextContent();
        int result = Integer.parseInt(sessionID);
        return new MessagePair(message, result);
    }
}
