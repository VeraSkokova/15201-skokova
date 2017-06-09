package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
              System.out.println("Add configuration file");
            } else {
              String name = args[0];
              ConfigParser configParser= new ConfigParser(name);

              FactoryController factoryController = new FactoryController(configParser);
              factoryController.runFactory();

              Thread.sleep(10);

              factoryController.interruptFactory();
          }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
