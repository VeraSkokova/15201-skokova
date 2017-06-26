package ru.nsu.ccfit.skokova.chat.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.nsu.ccfit.skokova.chat.ClientPair;
import ru.nsu.ccfit.skokova.chat.MessagePair;

public class XMLMessageParser {
    public void parseMessage(Document document) {
        Node root = document.getDocumentElement();
        Element temp = (Element)root;
        switch (root.getNodeName()) {
            case "command":
                switch (temp.getAttribute("name")) {
                    case "list": //TODO : process like UserListMessage
                    case "login":
                    case "logout":
                    case "message":
                }
                break;
            case "event":
                switch (temp.getAttribute("name")) {
                    case "message":
                        String serverMessage = parseServerMessage(temp);
                        break;
                    case "userlogin":
                        String userLoginEvent = parseUserLoginEvent(temp);
                        break;
                    case "userlogout":
                        String userLogoutEvent = parseUserLogoutEvent(temp);
                        break;
                }
                break;
            case "error":
                String serverErrorMessage = parseServerErrorMessage(temp);
                break;
            case "success":

        }
    }

    private String parseServerMessage(Element element) {
        String message = element.getElementsByTagName("message").item(0).getTextContent();
        String username = element.getElementsByTagName("name").item(0).getTextContent();

        return username + ": " + message;
    }

    private String parseUserLoginEvent(Element element) {
        String username = element.getElementsByTagName("name").item(0).getTextContent();
        return username + " logged in";
    }

    private String parseUserLogoutEvent(Element element) {
        String username = element.getElementsByTagName("name").item(0).getTextContent();
        return username + " logged out";
    }

    private String parseServerErrorMessage(Element element) {
        String reason = element.getElementsByTagName("message").item(0).getTextContent();
        return "Error: " + reason;
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
