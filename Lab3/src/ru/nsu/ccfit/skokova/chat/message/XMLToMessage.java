package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.skokova.chat.ClientPair;
import ru.nsu.ccfit.skokova.chat.MessagePair;
import ru.nsu.ccfit.skokova.chat.Server;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XMLToMessage {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    public static ChatMessage parseMessage(InputSource inputSource) throws ParserConfigurationException, IOException, SAXException {
        ChatMessage message = null;
        Document document = documentBuilderFactory.newDocumentBuilder().parse(inputSource);
        Node root = document.getDocumentElement();
        Element temp = (Element) root;
        switch (root.getNodeName()) {
            case "command":
                switch (temp.getAttribute("name")) {
                    case "list":
                        int sessionID = parseSimpleCommand(temp);
                        message = new UserListMessage(sessionID);
                        break;
                    case "login":
                        ClientPair clientPair = parseLoginCommand(temp);
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
        return message;
    }

    private static int parseSimpleCommand(Element element) {
        String sessionID = element.getElementsByTagName("session").item(0).getTextContent();
        return Integer.parseInt(sessionID);
    }

    private static ClientPair parseLoginCommand(Element element) {
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        String type = element.getElementsByTagName("type").item(0).getTextContent();
        return new ClientPair(name, type);
    }

    private static MessagePair parseMessageCommand(Element element) {
        String message = element.getElementsByTagName("message").item(0).getTextContent();
        String sessionID = element.getElementsByTagName("session").item(0).getTextContent();
        int result = Integer.parseInt(sessionID);
        return new MessagePair(message, result);
    }
}
