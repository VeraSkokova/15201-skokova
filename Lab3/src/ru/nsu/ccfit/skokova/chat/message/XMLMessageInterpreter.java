package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.skokova.chat.Client;
import ru.nsu.ccfit.skokova.chat.XMLClient;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class XMLMessageInterpreter {
    private static final Logger logger = LogManager.getLogger(XMLClient.class);
    private Client client;

    public XMLMessageInterpreter(Client client) {
        this.client = client;
    }

    public ServerMessage interpret(String message) {
        ServerMessage serverMessage = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8))));
            Node root = document.getDocumentElement();
            Element element = (Element)root;

            switch (root.getNodeName()) {
                case "error":
                    serverMessage = parseServerErrorMessage(element);
                    break;
                case "event":
                    switch (element.getAttribute("name")) {
                        case "message":
                            serverMessage = parseServerMessage(element);
                            break;
                        case "userlogin":
                            serverMessage = parseUserLoginEvent(element);
                            break;
                        case "userlogout":
                            serverMessage = parseUserLogoutEvent(element);
                            break;
                    }
                    break;
                case "command":
                    switch (element.getAttribute("name")) {
                        case "message":
                            serverMessage = parseYourselfMessage(element);
                            break;
                    }
                    break;
                case "success":
                    if (element.getChildNodes().item(0) == null) {
                        serverMessage = parseServerSuccessMessage(element);
                    } else {
                        switch (element.getChildNodes().item(0).getNodeName()) {
                            case "listusers":
                                serverMessage = parseUserListSuccess(element);
                                break;
                            case "session":
                                serverMessage = parseLoginSuccessMessage(element);
                                break;
                        }
                    }
                    break;
            }

            if (serverMessage == null) {
                throw new NullPointerException();
            }
        } catch (ParserConfigurationException | SAXException e) {
            logger.error("Can't parse");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (NullPointerException e) {
            logger.error("Bad type of message");
        }
        return serverMessage;
    }

    private TextMessageFromServer parseServerMessage(Element element) {
        String message = element.getElementsByTagName("message").item(0).getTextContent();
        String username = element.getElementsByTagName("name").item(0).getTextContent();

        return new TextMessageFromServer(message, username);
    }

    private TextMessageFromServer parseYourselfMessage(Element element) {
        String message = element.getElementsByTagName("message").item(0).getTextContent();

        return new TextMessageFromServer(message, "You");
    }

    private NewClientMessage parseUserLoginEvent(Element element) {
        String username = element.getElementsByTagName("name").item(0).getTextContent();
        logger.debug("In interpreter username is " + username);
        return new NewClientMessage(username);
    }

    private ClientLoggedOutMessage parseUserLogoutEvent(Element element) {
        String username = element.getElementsByTagName("name").item(0).getTextContent();
        return new ClientLoggedOutMessage(username);
    }

    private ServerErrorMessage parseServerErrorMessage(Element element) {
        String reason = element.getElementsByTagName("message").item(0).getTextContent();
        return new ServerErrorMessage(reason);
    }

    private LoginSuccess parseLoginSuccessMessage(Element element) {
        String sessionId = element.getElementsByTagName("session").item(0).getTextContent();
        int sessionID = Integer.parseInt(sessionId);
        client.setLoggedIn(true);
        client.setSessionId(sessionID);
        LoginSuccess loginSuccess = new LoginSuccess(sessionID);
        loginSuccess.setMessage("Welcome!");
        return loginSuccess;
    }

    public ServerSuccessMessage parseServerSuccessMessage(Element element) {
        return new ServerSuccessMessage();
    }

    public UserListSuccess parseUserListSuccess(Element element) {
        String userList = "\nList of the users:\n";
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() != Node.TEXT_NODE) {
                NodeList nodes = node.getChildNodes();
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node listuser = nodes.item(j);
                    if (listuser.getNodeType() != Node.TEXT_NODE) {
                        NodeList users = listuser.getChildNodes();
                        userList += j + ". ";
                        for (int k = 0; k < users.getLength(); k++) {
                            Node user = users.item(k);
                            if (user.getNodeType() != Node.TEXT_NODE) {
                                userList += user.getChildNodes().item(0).getTextContent() + " ";
                            }
                        }
                        userList += "\n";
                    }
                }
            }
        }
        return new UserListSuccess(userList);
    }
}
