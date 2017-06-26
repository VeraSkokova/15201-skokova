package ru.nsu.ccfit.skokova.chat.message;

import java.util.List;

public class XMLMessageCreator {
    public XMLMessageCreator() {}

    public String createLoginMessage(String username, String type) {
        return "<command name=\"login\">\n" +
                "<name>" + username + "</name>\n" +
                "<type>" + type + "</type>\n" +
                "</command>";
    }

    public String createServerErrorMessage(String reason) {
        return "<error>\n" +
                "<message>" + reason + "</message>\n" +
                "</error>";
    }

    public String createServerSuccessMessage() {
        return "<success>\n</success>";
    }

    public String createLoginSuccessMessage(String sessionID) {
        return "<success>\n" +
                "<session>" + sessionID + "</session>\n" +
                "</success>";
    }

    public String createUserListRequestMessage(String sessionId) {
        return "<command name=\"list\">\n" +
                "<session>" + sessionId + "</session>\n" +
                "</command>";
    }

    public String createUserListMessage(List<String> usernames, List<String> types) {
        String result = "<success>\n" +
                "<listusers>\n";
        for (int i = 0; i < usernames.size(); i++) {
            String temp = createUserInfo(usernames.get(i), types.get(i));
            result += temp;
        }
        result += "</listusers>\n" + "</success>";
        return result;
    }

    public String createClientMessage(String message, String sessionID) {
        return "<command name=\"message\">\n" +
                "<message>" + message + "</message>\n" +
                "<session>" + sessionID + "</session>\n" +
                "</command>";
    }

    public String createServerMessage(String message, String sender) {
        return "<event name=\"message\">\n" +
                "<message>" + message + "</message>\n" +
                "<name>" + sender + "</name>\n" +
                "</event>";
    }

    public String createNewClientMessage(String username) {
        return "<event name=\"userlogin\">\n" +
                "<name>" + username + "</name>\n" +
                "</event>";
    }

    public String createLogoutMessage(String username) {
        return "<event name=\"userlogout\">\n" +
                "<name>" + username + "</name>\n" +
                "</event>";
    }

    private String createUserInfo(String username, String type) {
        return "<user>\n" +
                "<name>" + username + "</name>\n" +
                "<type>" + type + "</type>\n" +
                "</user>";
    }
}
