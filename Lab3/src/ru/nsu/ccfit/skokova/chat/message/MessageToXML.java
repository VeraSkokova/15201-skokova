package ru.nsu.ccfit.skokova.chat.message;

public class MessageToXML {
    private static final XMLMessageCreator xmlMessageCreator = new XMLMessageCreator();

    public static String parseMessage(ServerMessage serverMessage) {
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
                result = xmlMessageCreator.createNewClientMessage(serverMessage.getMessage(), ((NewClientMessage) serverMessage).getType());
                break;
            case "ClientLoggedOutMessage":
                result = xmlMessageCreator.createClientLoggedOutMessage(serverMessage.getUsername());
                break;
            case "TextMessageFromServer":
                TextMessageFromServer textMessageFromServer = (TextMessageFromServer) serverMessage;
                result = xmlMessageCreator.createServerMessage(textMessageFromServer.getMessage(), textMessageFromServer.getUsername());
                break;
        }
        return result;
    }
}

