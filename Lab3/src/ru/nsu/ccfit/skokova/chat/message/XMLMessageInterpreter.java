package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.skokova.chat.XMLClient;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class XMLMessageInterpreter {
    private static final Logger logger = LogManager.getLogger(XMLClient.class);

    public XMLMessageInterpreter() {
    }

    public ServerMessage interpret(String message) {
        ServerMessage serverMessage = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8))));
            Element element = document.getDocumentElement();
            String root = element.getTagName();

            switch (root) {
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
                    NodeList users = document.getElementsByTagName("user");
                    if (users.getLength() != 0) {
                        serverMessage = parseUserListSuccess(users);
                    } else if (document.getElementsByTagName("session").item(0) != null) {
                        serverMessage = parseLoginSuccessMessage(element);
                    } else {
                        serverMessage = parseServerSuccessMessage();
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
        String type = element.getElementsByTagName("type").item(0).getTextContent();
        return new NewClientMessage(username, type);
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
        LoginSuccess loginSuccess = new LoginSuccess(sessionID);
        loginSuccess.setMessage("Welcome!");
        return loginSuccess;
    }

    private ServerSuccessMessage parseServerSuccessMessage() {
        return new ServerSuccessMessage();
    }

    private UserListSuccess parseUserListSuccess(NodeList nodeList) {
        String userList = "\nList of the users\n";
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element user = (Element) nodeList.item(i);
            userList += "-" + user.getElementsByTagName("name").item(0).getTextContent() + " " + user.getElementsByTagName("type").item(0).getTextContent() + "\n";
        }
        return new UserListSuccess(userList);
    }
}
