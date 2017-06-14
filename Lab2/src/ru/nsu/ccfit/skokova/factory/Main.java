package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.factory.gui.CreateAndShowGUI;
import ru.nsu.ccfit.skokova.factory.gui.FactoryButtons;
import ru.nsu.ccfit.skokova.factory.gui.FactoryInformer;
import ru.nsu.ccfit.skokova.factory.gui.FactoryRegulator;
import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Add configuration file");
            } else {
                String name = args[0];
                ConfigParser configParser = new ConfigParser(name);

                FactoryController factoryController = new FactoryController(configParser);

                Controller controller = new Controller(factoryController);


            } catch(IOException | BadParseException | NumberFormatException e){
                logger.error(e.getMessage());
            }
        }
    }
}
