package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigParser {
    private String fileName;

    static Map<String, Integer> map = new HashMap<>();
    private boolean enableLogs;

    private static Logger logger = LogManager.getLogger(ConfigParser.class);

    static {
        map.put("AccessoryStorageSize", 0);
        map.put("BodyStorageSize", 0);
        map.put("EngineStorageSize", 0);
        map.put("CarStorageSize", 0);
        map.put("AccessorySuppliersCount", 0);
        map.put("WorkersCount", 0);
        map.put("DealersCount", 0);
        map.put("EngineSupplierPeriodicity", 0);
        map.put("BodySupplierPeriodicity", 0);
        map.put("AccessorySupplierPeriodicity", 0);
        map.put("DealersPeriodicity", 0);
        map.put("TaskQueueSize", 0);
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

    public boolean isEnableLogs() {
        return enableLogs;
    }

    public static Map<String, Integer> getMap() {
        return map;
    }
}

class BadParseException extends Exception {
    BadParseException(String message) {
        super(message);
    }
}
