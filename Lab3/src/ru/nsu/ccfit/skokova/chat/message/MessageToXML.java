package ru.nsu.ccfit.skokova.chat.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

public class MessageToXML {
    private static final Logger logger = LogManager.getLogger(MessageToXML.class);

    private XMLMessageCreator xmlMessageCreator = new XMLMessageCreator();

    public String parseMessage(ServerMessage serverMessage) {
        String result = "";
        switch (serverMessage.getClass().getSimpleName()) {
            case "LoginError":
            case "ListError":
            case "TextMessageToServerError":
            case "LogoutError":
                result = xmlMessageCreator.createServerErrorMessage(serverMessage.getMessage());
                break;
            case "LoginSuccess":
                LoginSuccess loginSuccess = (LoginSuccess) serverMessage;
                result = xmlMessageCreator.createLoginSuccessMessage(Integer.toString(loginSuccess.getSessionId()));
                break;
            case "UserListSuccess":
                UserListSuccess userListSuccess = (UserListSuccess) serverMessage;
                result = xmlMessageCreator.createUserListMessage(userListSuccess.getUsernames(), userListSuccess.getTypes());
                break;
            case "TextMessageToServerSuccess":
            case "LogoutSuccess":
                result = xmlMessageCreator.createServerSuccessMessage();
                break;
            case "NewClientMessage":
                result = xmlMessageCreator.createNewClientMessage(serverMessage.getMessage());
                break;
            case "ClientLoggedOutMessage":
                result = xmlMessageCreator.createClientLoggedOutMessage(serverMessage.getMessage());
                break;
            case "TextMessageFromServer":
                TextMessageFromServer textMessageFromServer = (TextMessageFromServer) serverMessage;
                result = xmlMessageCreator.createServerMessage(textMessageFromServer.getSentMessage(), textMessageFromServer.getSender());
                break;
        }
        return result;
    }
}

