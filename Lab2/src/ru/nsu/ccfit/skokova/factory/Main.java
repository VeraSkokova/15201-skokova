package ru.nsu.ccfit.skokova.factory;

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
    public static void main(String[] args) {
        try {
            String name = "/home/veraskokova/Документы/MyConfig.txt";
            ConfigParser configParser= new ConfigParser(name);

            FactoryController factoryController = new FactoryController(configParser);

            Controller controller = new Controller(factoryController);

            //factoryController.runFactory();

            //Thread.sleep(19000);

            //factoryController.interruptFactory();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
