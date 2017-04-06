package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.Map;



public class Main {

    public static void main(String[] args) {

        try {
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.ExtentionFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.EarlierTimeFilterSerializer");
            Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.LaterTimeFilterSerializer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String dirPath = "/home/veraskokova/Документы/Testing";
        String configFile = "/home/veraskokova/Документы/Testing/config.txt";
        Controller controller = new Controller(dirPath, configFile);
        try {
            controller.fillStatisctics();
            ConfigParser configParser = new ConfigParser("/home/veraskokova/Документы/Testing/config.txt");

        }
        catch (IOException ex) {
            System.out.println("Oops");
        }


        System.out.println("Total: ");

    }

}



