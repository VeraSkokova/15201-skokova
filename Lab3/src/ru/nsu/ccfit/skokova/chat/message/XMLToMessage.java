package ru.nsu.ccfit.skokova.chat.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.nsu.ccfit.skokova.chat.ClientPair;
import ru.nsu.ccfit.skokova.chat.MessagePair;

public class XMLToMessage {
    public Message parseMessage(Document document) {
        Node root = document.getDocumentElement();
        Element temp = (Element)root;
        switch (root.getNodeName()) {
            case "command":
                switch (temp.getAttribute("name")) {
                    case "list":
                        int sessionID = parseSimpleCommand(temp);

                    case "login":
                        ClientPair clientPair = parseLogincommand(temp);

                    case "logout":
                    case "message":
                }
                break;
            default:
                break;
        }
        return new TextMessageFromServer("a"); /////!!!!!!!!
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
