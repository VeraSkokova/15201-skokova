package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main {
    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
              System.out.println("Add configuration file");
            } else {
              String name = args[0];
              ConfigParser configParser = new ConfigParser(name);

              FactoryController factoryController = new FactoryController(configParser);
              Controller controller = new Controller(factoryController);
          }


        } catch (IOException | BadParseException | NumberFormatException e) {
            logger.error(e.getMessage());
        }

    }
}
