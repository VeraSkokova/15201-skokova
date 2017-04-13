package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;

public class Main {

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

        String dirPath = "/home/veraskokova/Документы/Testing";
        String configFile = "/home/veraskokova/Документы/Testing/config.txt";
        Controller controller = new Controller(dirPath, configFile);
        try {
            controller.fillStatisctics();
            controller.printStatistics();
        }
        catch (IOException ex) {
            System.out.println("Oops");
        }


    }

}



