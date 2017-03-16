package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        String dirPath = "/home/veraskokova/Документы/Testing";
        Controller controller = new Controller(dirPath);
        try {
            controller.fillStatisctics();
        }
        catch (IOException ex) {
            System.out.println("Oops");
        }

        System.out.println("Total: ");

    }


}


