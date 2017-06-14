package ru.nsu.ccfit.skokova.factory.gui;

import ru.nsu.ccfit.skokova.factory.ConfigParser;
import ru.nsu.ccfit.skokova.factory.FactoryController;

import javax.swing.*;
import java.awt.*;

public class CreateAndShowGUI extends JFrame {
    private JPanel regulationPanel = new JPanel();
    private JPanel informationPanel = new JPanel();
    private JPanel staticInformationPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private FactoryController factoryController;

    public CreateAndShowGUI(FactoryController factoryController) throws HeadlessException {
        this.factoryController = factoryController;
        this.setName("Factory");
    }

    private void addRegulators() {
        JLabel engineSupplierPeriodicityLabel = new JLabel("EngineSupplier Periodicity");
        engineSupplierPeriodicityLabel.setPreferredSize(new Dimension(330, 50));

        JLabel bodySupplierPeriodicityLabel = new JLabel("BodySupplier Periodicity");
        bodySupplierPeriodicityLabel.setPreferredSize(new Dimension(330, 50));

        JLabel accessorySuppliersPeriodicityLabel = new JLabel("AccessorySupplier's Periodicity");
        accessorySuppliersPeriodicityLabel.setPreferredSize(new Dimension(330, 50));

        JLabel dealersPeriodocityLabel = new JLabel("Dealer's Periodicity");
        dealersPeriodocityLabel.setPreferredSize(new Dimension(330, 50));

        FactoryRegulator engineSupplierSlider = new FactoryRegulator(engineSupplierPeriodicityLabel, 0 ,5000, (Integer.toString(ConfigParser.getMap().get("EngineSupplierPeriodicity"))));
        FactoryRegulator bodySupplierSlider = new FactoryRegulator(bodySupplierPeriodicityLabel, 0, 5000, (Integer.toString(ConfigParser.getMap().get("BodySupplierPeriodicity"))));
        FactoryRegulator accessorySupplierSlider = new FactoryRegulator(accessorySuppliersPeriodicityLabel, 0, 5000, (Integer.toString(ConfigParser.getMap().get("AccessorySupplierPeriodicity"))));
        FactoryRegulator dealersSlider = new FactoryRegulator(dealersPeriodocityLabel, 0, 5000, (Integer.toString(ConfigParser.getMap().get("DealersPeriodicity"))));

        regulationPanel.add(engineSupplierSlider);
        regulationPanel.add(bodySupplierSlider);
        regulationPanel.add(accessorySupplierSlider);
        regulationPanel.add(dealersSlider);

        this.getContentPane().add(BorderLayout.WEST, regulationPanel);
    }

    private void addInformers() {
        FactoryInformer first = new FactoryInformer(new JLabel("Engines on EngineStorage"), new JLabel("0"));
        first.setPreferredSize(new Dimension(330, 50));

        FactoryInformer second = new FactoryInformer(new JLabel("Bodies on BodyStorage"), new JLabel("0"));
        second.setPreferredSize(new Dimension(330, 50));

        FactoryInformer third = new FactoryInformer(new JLabel("Accessories on AccessoryStorage"), new JLabel("0"));
        third.setPreferredSize(new Dimension(330, 50));

        FactoryInformer fourth = new FactoryInformer(new JLabel("Cars Made"), new JLabel("0"));
        fourth.setPreferredSize(new Dimension(330, 50));

        FactoryInformer fifth = new FactoryInformer(new JLabel("Cars on CarStorage"), new JLabel("0"));
        fifth.setPreferredSize(new Dimension(330, 50));

        FactoryInformer sixth = new FactoryInformer(new JLabel("Cars Sold"), new JLabel("0"));
        sixth.setPreferredSize(new Dimension(330, 50));

        FactoryInformer seventh = new FactoryInformer(new JLabel("Tasks to do"), new JLabel("0"));
        seventh.setPreferredSize(new Dimension(330, 50));

        informationPanel.add(first);
        informationPanel.add(second);
        informationPanel.add(third);
        informationPanel.add(fourth);
        informationPanel.add(fifth);
        informationPanel.add(sixth);
        informationPanel.add(seventh);

        this.getContentPane().add(BorderLayout.EAST, informationPanel);

    }

    private void addStaticInformation() {
        JPanel storageSizes = new JPanel();
        FactoryInformer engineStorageSizeLabel = new FactoryInformer(new JLabel("EngineStorage Size"), new JLabel((Integer.toString(ConfigParser.getMap().get("EngineStorageSize")))));
        engineStorageSizeLabel.setPreferredSize(new Dimension(330, 50));

        FactoryInformer bodyStorageSizeLabel = new FactoryInformer(new JLabel("BodyStorage Size"), new JLabel(Integer.toString(ConfigParser.getMap().get("BodyStorageSize"))));
        bodyStorageSizeLabel.setPreferredSize(new Dimension(330, 50));

        FactoryInformer accessoryStorageSizePanel = new FactoryInformer(new JLabel("AccessoryStorage Size"), new JLabel(Integer.toString(ConfigParser.getMap().get("AccessoryStorageSize"))));
        accessoryStorageSizePanel.setPreferredSize(new Dimension(330, 50));

        FactoryInformer carStorageSizePanel = new FactoryInformer(new JLabel("CarStorage Size"), new JLabel(Integer.toString(ConfigParser.getMap().get("CarStorageSize"))));
        carStorageSizePanel.setPreferredSize(new Dimension(330, 50));

        storageSizes.add(engineStorageSizeLabel);
        storageSizes.add(bodyStorageSizeLabel);
        storageSizes.add(accessoryStorageSizePanel);
        storageSizes.add(carStorageSizePanel);

        JPanel employeesCount = new JPanel();
        FactoryInformer accessorySuppliersCountLabel = new FactoryInformer(new JLabel("AccessorySuppliers Count"), new JLabel(Integer.toString(ConfigParser.getMap().get("AccessorySuppliersCount"))));
        accessorySuppliersCountLabel.setPreferredSize(new Dimension(330, 50));

        FactoryInformer workersCountLabel = new FactoryInformer(new JLabel("Workers Count"), new JLabel(Integer.toString(ConfigParser.getMap().get("WorkersCount"))));
        workersCountLabel.setPreferredSize(new Dimension(330, 50));

        FactoryInformer dealersCountLabel = new FactoryInformer(new JLabel("Dealers Count"), new JLabel(Integer.toString(ConfigParser.getMap().get("DealersCount"))));
        dealersCountLabel.setPreferredSize(new Dimension(330, 50));

        employeesCount.add(accessorySuppliersCountLabel);
        employeesCount.add(workersCountLabel);
        employeesCount.add(dealersCountLabel);

        staticInformationPanel.add(storageSizes);
        staticInformationPanel.add(employeesCount);

        this.getContentPane().add(BorderLayout.NORTH, staticInformationPanel);
    }

    private void addButtons() {
        FactoryButtons factoryButtons = new FactoryButtons();

        buttonPanel.add(factoryButtons);

        this.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
    }

    public void createAndShowGUI() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        regulationPanel.setLayout(new BoxLayout(regulationPanel, BoxLayout.Y_AXIS));
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));

        addRegulators();
        addInformers();
        addButtons();
        addStaticInformation();

        staticInformationPanel.setPreferredSize(new Dimension(660, 110));

        this.setSize(1200, 600);
        this.setVisible(true);
    }

    public void dispose() {
        factoryController.interruptFactory();
        super.dispose();
        System.exit(0);
    }

    public JPanel getRegulationPanel() {
        return regulationPanel;
    }

    public JPanel getInformationPanel() {
        return informationPanel;
    }

    public JPanel getButtonPanel() {
        return this.buttonPanel;
    }
}
