package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ConfigParser {
    private String configFileName;
    private ArrayList<String> filterStrings;

    public ArrayList<String> getFilterStrings() {
        return filterStrings;
    }

    public ConfigParser(String fileName) throws IOException {
        this.configFileName = fileName;
        this.filterStrings = new ArrayList<>(0);
        File file = new File(this.configFileName);
        FileReader helper = new FileReader(file);
        BufferedReader reader = new BufferedReader(helper);
        String temp = reader.readLine();
        while (temp != null) {
            if (!Objects.equals(temp, "")) {
                this.filterStrings.add(temp);
            }
            temp = reader.readLine();
        }
    }
}
