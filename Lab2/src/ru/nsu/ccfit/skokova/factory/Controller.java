package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.CreateAndShowGUI;
import ru.nsu.ccfit.skokova.factory.gui.FactoryButtons;
import ru.nsu.ccfit.skokova.factory.gui.FactoryInformer;
import ru.nsu.ccfit.skokova.factory.gui.FactoryRegulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private FactoryController factoryController;

    public Controller(FactoryController factoryController) {
        this.factoryController = factoryController;

        CreateAndShowGUI.createAndShowGUI();


        /***********Information Panel***********/
        FactoryInformer first = (FactoryInformer) CreateAndShowGUI.getInformationPanel().getComponent(0);
        factoryController.getEngineStorage().addHandler(first.new ValueChanger());

        FactoryInformer second = (FactoryInformer) CreateAndShowGUI.getInformationPanel().getComponent(1);
        factoryController.getBodyStorage().addHandler(second.new ValueChanger());

        FactoryInformer third = (FactoryInformer) CreateAndShowGUI.getInformationPanel().getComponent(2);
        factoryController.getAccessoryStorage().addHandler(third.new ValueChanger());

        /***********Regulation Panel***********/


        /***********Buttons***********/
        FactoryButtons factoryButtons = (FactoryButtons) CreateAndShowGUI.getButtonPanel().getComponent(0);
        JButton runButton = factoryButtons.getRunButton();
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                factoryController.runFactory();
            }
        });

        JButton stopButton = factoryButtons.getStopButton();
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                factoryController.interruptFactory();
            }
        });
    }

}
