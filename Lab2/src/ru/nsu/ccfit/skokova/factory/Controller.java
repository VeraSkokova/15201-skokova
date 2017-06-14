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

        CreateAndShowGUI createAndShowGUI = new CreateAndShowGUI(factoryController);

        createAndShowGUI.createAndShowGUI();

        /***********Information Panel***********/
        FactoryInformer enginesCountInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(0);
        factoryController.getEngineStorage().addHandler(enginesCountInformer.new ValueChanger());

        FactoryInformer bodiesCountInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(1);
        factoryController.getBodyStorage().addHandler(bodiesCountInformer.new ValueChanger());

        FactoryInformer accessoriesCountInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(2);
        factoryController.getAccessoryStorage().addHandler(accessoriesCountInformer.new ValueChanger());

        FactoryInformer carsMadeInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(3);
        Worker.addHandler(carsMadeInformer.new ValueChanger());

        FactoryInformer carsOnCarStorageInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(4);
        factoryController.getCarStorage().addHandler(carsOnCarStorageInformer.new ValueChanger());

        FactoryInformer carsSoldInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(5);
        Dealer.addHandler(carsSoldInformer.new ValueChanger());

        FactoryInformer tasksToDoInformer = (FactoryInformer) createAndShowGUI.getInformationPanel().getComponent(6);
        factoryController.getThreadPool().addHandler(tasksToDoInformer.new ValueChanger());

        /***********Regulation Panel***********/
        FactoryRegulator engineSupplierRegulator = (FactoryRegulator) createAndShowGUI.getRegulationPanel().getComponent(0);
        EngineSupplierUpdater engineSupplierUpdater = new EngineSupplierUpdater(this.factoryController.getEngineSupplier());
        engineSupplierRegulator.addHandler(engineSupplierUpdater);

        FactoryRegulator bodySupplierRegulator = (FactoryRegulator) createAndShowGUI.getRegulationPanel().getComponent(1);
        BodySupplierUpdater bodySupplierUpdater = new BodySupplierUpdater(this.factoryController.getBodySupplier());
        bodySupplierRegulator.addHandler(bodySupplierUpdater);

        FactoryRegulator accessorySupplierRegulator = (FactoryRegulator) createAndShowGUI.getRegulationPanel().getComponent(2);
        AccessorySupplierUpdater accessorySupplierUpdater = new AccessorySupplierUpdater(this.factoryController.getAccessorySuppliers());
        accessorySupplierRegulator.addHandler(accessorySupplierUpdater);

        FactoryRegulator dealersRegulator = (FactoryRegulator) createAndShowGUI.getRegulationPanel().getComponent(3);
        DealerUpdater dealerUpdater = new DealerUpdater(this.factoryController.getDealers());
        dealersRegulator.addHandler(dealerUpdater);

        /***********Buttons***********/
        FactoryButtons factoryButtons = (FactoryButtons) createAndShowGUI.getButtonPanel().getComponent(0);
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

    public void dispose() {
        factoryController.interruptFactory();
        System.exit(0);
    }

}
