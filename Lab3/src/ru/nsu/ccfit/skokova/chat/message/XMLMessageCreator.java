package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.nsu.ccfit.skokova.chat.Server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class XMLMessageCreator {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private DocumentBuilder documentBuilder;
    private Transformer transformer;

    public XMLMessageCreator() {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.displayName());
        } catch (ParserConfigurationException e) {
            logger.error("Exception in Parser Configuration");
        } catch (TransformerConfigurationException e) {
            logger.error("Exception in transformer configuration");
        }
    }

    public String createLoginMessage(String username, String type) {
        Document document = documentBuilder.newDocument();
        Element command = document.createElement("command");
        document.appendChild(command);
        command.setAttribute("name", "login");

        Element name = document.createElement("name");
        command.appendChild(name);
        name.setTextContent(username);

        Element typeElement = document.createElement("type");
        command.appendChild(typeElement);
        typeElement.setTextContent(type);

        return createString(document);
    }

    public String createServerErrorMessage(String reason) {
        Document document = documentBuilder.newDocument();
        Element error = document.createElement("error");
        document.appendChild(error);

        Element message = document.createElement("message");
        error.appendChild(message);
        message.setTextContent(reason);

        return createString(document);
    }

    public String createServerSuccessMessage() {
        Document document = documentBuilder.newDocument();
        Element success = document.createElement("success");
        document.appendChild(success);
        return createString(document);
    }

    public String createLoginSuccessMessage(String sessionID) {
        Document document = documentBuilder.newDocument();
        Element success = document.createElement("success");
        document.appendChild(success);

        Element session = document.createElement("session");
        success.appendChild(session);
        session.setTextContent(sessionID);

        return createString(document);
    }

    public String createUserListRequestMessage(String sessionId) {
        Document document = documentBuilder.newDocument();
        Element command = document.createElement("command");
        document.appendChild(command);
        command.setAttribute("name", "list");

        Element session = document.createElement("session");
        command.appendChild(session);
        session.setTextContent(sessionId);

        return createString(document);
    }

    public String createUserListMessage(List<String> usernames, List<String> types) {
        Document document = documentBuilder.newDocument();
        Element success = document.createElement("success");
        document.appendChild(success);

        Element listusers = document.createElement("listusers");
        success.appendChild(listusers);

        for (int i = 0; i < usernames.size(); i++) {
            Element user = document.createElement("user");
            listusers.appendChild(user);
            Element name = document.createElement("name");
            user.appendChild(name);
            name.setTextContent(usernames.get(i));
            Element type = document.createElement("type");
            user.appendChild(type);
            type.setTextContent(types.get(i));
        }

        return createString(document);
    }

    public String createClientMessage(String message, String sessionID) {
        Document document = documentBuilder.newDocument();
        Element command = document.createElement("command");
        document.appendChild(command);
        command.setAttribute("name", "message");

        Element messageElement = document.createElement("message");
        command.appendChild(messageElement);
        messageElement.setTextContent(message);

        Element sessionElement = document.createElement("session");
        command.appendChild(sessionElement);
        sessionElement.setTextContent(sessionID);

        return createString(document);
    }

    public String createServerMessage(String message, String sender) {
        Document document = documentBuilder.newDocument();
        Element event = document.createElement("event");
        document.appendChild(event);
        event.setAttribute("name", "message");

        Element messageElement = document.createElement("message");
        event.appendChild(messageElement);
        messageElement.setTextContent(message);

        Element nameElement = document.createElement("name");
        event.appendChild(nameElement);
        nameElement.setTextContent(sender);

        return createString(document);
    }

    public String createNewClientMessage(String username, String type) {
        Document document = documentBuilder.newDocument();
        Element event = document.createElement("event");
        document.appendChild(event);
        event.setAttribute("name", "userlogin");

        Element name = document.createElement("name");
        event.appendChild(name);
        name.setTextContent(username);

        Element typeElement = document.createElement("type");
        event.appendChild(typeElement);
        typeElement.setTextContent(type);

        return createString(document);
    }

    public String createLogoutMessage(String sessionId) {
        Document document = documentBuilder.newDocument();
        Element command = document.createElement("command");
        document.appendChild(command);
        command.setAttribute("name", "logout");

        Element session = document.createElement("session");
        command.appendChild(session);
        session.setTextContent(sessionId);

        return createString(document);
    }

    public String createClientLoggedOutMessage(String username) {
        Document document = documentBuilder.newDocument();
        Element event = document.createElement("event");
        document.appendChild(event);
        event.setAttribute("name", "userlogout");

        Element name = document.createElement("name");
        event.appendChild(name);
        name.setTextContent(username);

        return createString(document);
    }

    private String createString(Document document) {
        DOMSource domSource = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            logger.error("Transformer exception: " + e.getMessage());
        }
        return stringWriter.toString();
    }
}
