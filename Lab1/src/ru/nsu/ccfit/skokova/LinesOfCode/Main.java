package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;

public class Main {
    static void printUsage() {
        try {
            File file = new File("../src/Usage.txt");
            FileReader helper = new FileReader(file);
            BufferedReader reader = new BufferedReader(helper);
            String temp = reader.readLine();
            while (temp != null) {
                System.out.println(temp);
                temp = reader.readLine();
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Can't print Usage: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.ExtentionFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.EarlierTimeFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.LaterTimeFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.AndFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.OrFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.NotFilterSerializer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (args.length != 2) {
            printUsage();
        } else {
            String dirPath = args[1];
            String configFile = args[0];
            Controller controller = new Controller(dirPath, configFile);
            try {
                controller.fillStatisctics();
                controller.printStatistics();
            } catch (IOException e) {
                System.err.println("Something went wrong: " + e.getMessage());
            }
        }

    }

}
