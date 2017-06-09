package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            String name = "/home/veraskokova/Документы/MyConfig.txt";
            ConfigParser configParser= new ConfigParser(name);

            FactoryController factoryController = new FactoryController(configParser);
            factoryController.runFactory();

            Thread.sleep(10);

            factoryController.interruptFactory();


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
