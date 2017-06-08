package ru.nsu.ccfit.skokova.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigParser {
    private static ArrayList<ConfigPair> configPairs = new ArrayList<>();
    private boolean enableLogs;
    private String fileName;

    static {
        configPairs.add(new ConfigPair(0, "EngineStorageSize"));
        configPairs.add(new ConfigPair(0, "BodyStorageSize"));
        configPairs.add(new ConfigPair(0, "AccessoryStorageSize"));
        configPairs.add(new ConfigPair(0, "CarStorageSize"));
        configPairs.add(new ConfigPair(0, "AccessorySuppliersCount"));
        configPairs.add(new ConfigPair(0, "WorkersCount"));
        configPairs.add(new ConfigPair(0, "DealersCount"));
        configPairs.add(new ConfigPair(0, "EngineSupplierPeriodicity"));
        configPairs.add(new ConfigPair(0, "BodySupplierPeriodicity"));
        configPairs.add(new ConfigPair(0, "AccessorySupplierPeriodicity"));
        configPairs.add(new ConfigPair(0, "DealersPeriodicity"));
        configPairs.add(new ConfigPair(0, "TaskQueueSize"));
    }

    private static ArrayList<String> getFields() {
        ArrayList<String> result = new ArrayList<>();
        for (ConfigPair configPair : configPairs) {
            result.add(configPair.getName());
        }
        result.add("EnableLog");
        return result;
    }

    private void parseString(String string) {
        int num = 0;
        for (String s : ConfigParser.getFields()) {
            if (string.startsWith(s)) {
                String line = string.substring(string.indexOf(s) + s.length());
                line = line.trim();
                String anotherLine = line.substring(line.indexOf("=") + 1);
                anotherLine = anotherLine.trim();
                if (s.equals("EnableLog")) {
                    enableLogs = Boolean.parseBoolean(anotherLine);
                } else {
                    ConfigParser.configPairs.get(num).setValue(Integer.parseInt(anotherLine));
                }
                break;
            }
            num++;
        }
    }

    public ConfigParser(String fileName) throws IOException {
        this.fileName = fileName;
        File file = new File(this.fileName);
        try (FileReader helper = new FileReader(file);
             BufferedReader reader = new BufferedReader(helper)) {
            String temp = reader.readLine();
            while (temp != null) {
                temp = temp.trim();
                this.parseString(temp);
                temp = reader.readLine();
            }
        }
    }

    public static ArrayList<ConfigPair> getConfigPairs() {
        return configPairs;
    }

    public boolean isEnableLogs() {
        return enableLogs;
    }


}

class ConfigPair {
    private int value;
    private String name;

    ConfigPair(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    String getName() {
        return name;
    }

    void setValue(int value) {
        this.value = value;
    }
}
