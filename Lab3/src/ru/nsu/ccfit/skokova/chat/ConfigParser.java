package ru.nsu.ccfit.skokova.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigParser {
    static Map<String, Integer> map = new HashMap<>();
    private static Logger logger = LogManager.getLogger(ConfigParser.class);

    static {
        map.put("ObjectPort", 0);
        map.put("XMLPort", 0);
    }

    private String fileName;
    private boolean enableLogs;

    public ConfigParser(String fileName) throws IOException, BadParseException {
        this.fileName = fileName;
        File file = new File(this.fileName);
        try (FileReader str = new FileReader(file);
             BufferedReader reader = new BufferedReader(str)) {
            String temp = reader.readLine();
            while (temp != null) {
                temp = temp.trim();
                this.parseString(temp);
                temp = reader.readLine();
            }
        }
    }

    public void parseString(String str) throws BadParseException {
        int indexOfEq = str.indexOf('=');
        String line = str.substring(0, indexOfEq);
        line = line.trim();
        if (line.equals("EnableLog")) {
            String numberLine = str.substring(indexOfEq + 1);
            numberLine = numberLine.trim();
            this.enableLogs = Boolean.parseBoolean(numberLine);
        } else if (map.containsKey(line)) {
            String numberLine = str.substring(indexOfEq + 1);
            numberLine = numberLine.trim();
            int val = Integer.parseInt(numberLine);
            if (val < 0) {
                logger.error("Error in parsing ConfigFile");
                throw new BadParseException(line + " can't be " + val);
            }
            map.put(line, val);
        } else {
            logger.error("Error in parsing ConfigFile");
            throw new BadParseException("Unknown parameter" + line);
        }
    }

    public boolean isEnableLogs() {
        return enableLogs;
    }
}

class BadParseException extends Exception {
    BadParseException(String message) {
        super(message);
    }
}