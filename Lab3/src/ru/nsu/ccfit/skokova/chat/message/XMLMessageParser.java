package ru.nsu.ccfit.skokova.chat.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
}
