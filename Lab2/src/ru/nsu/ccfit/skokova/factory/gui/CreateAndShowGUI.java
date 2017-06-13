package ru.nsu.ccfit.skokova.factory.gui;

import javax.swing.*;
import java.awt.*;

public class CreateAndShowGUI {
    private static JFrame frame = new JFrame("Factory");
    private static JPanel regulationPanel = new JPanel();
    private static JPanel informationPanel = new JPanel();
    private static JPanel buttonPanel = new JPanel();

    private static void addRegulators() {
        JLabel first = new JLabel("EngineSupplier Periodicity");
        first.setPreferredSize(new Dimension(330, 50));

        JLabel second = new JLabel("BodySupplier Periodicity");
        second.setPreferredSize(new Dimension(330, 50));

        JLabel third = new JLabel("AccessorySupplier's Periodicity");
        third.setPreferredSize(new Dimension(330, 50));

        FactoryRegulator firstSlider = new FactoryRegulator(first, 0 ,100);
        FactoryRegulator secondSlider = new FactoryRegulator(second, 0, 100);
        FactoryRegulator thirdSlider = new FactoryRegulator(third, 0, 100);

        regulationPanel.add(firstSlider);
        regulationPanel.add(secondSlider);
        regulationPanel.add(thirdSlider);

        frame.getContentPane().add(BorderLayout.WEST, regulationPanel);
    }

    private static void addInformers() {
        FactoryInformer first = new FactoryInformer(new JLabel("Engines Count"), new JLabel("x"));
        first.setPreferredSize(new Dimension(330, 50));

        FactoryInformer second = new FactoryInformer(new JLabel("Bodies Count"), new JLabel("y"));
        second.setPreferredSize(new Dimension(330, 50));

        FactoryInformer third = new FactoryInformer(new JLabel("Accessories Count"), new JLabel("z"));
        third.setPreferredSize(new Dimension(330, 50));

        informationPanel.add(first);
        informationPanel.add(second);
        informationPanel.add(third);

        frame.getContentPane().add(BorderLayout.EAST, informationPanel);

    }

    private static void addButtons() {
        FactoryButtons factoryButtons = new FactoryButtons();

        buttonPanel.add(factoryButtons);

        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
    }

    public static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        regulationPanel.setLayout(new BoxLayout(regulationPanel, BoxLayout.Y_AXIS));
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));

        addRegulators();
        addInformers();
        addButtons();

        frame.setSize(1200, 600);
        frame.setVisible(true);
    }

    public static JPanel getRegulationPanel() {
        return regulationPanel;
    }

    public static JPanel getInformationPanel() {
        return informationPanel;
    }

    public static JPanel getButtonPanel() {
        return buttonPanel;
    }
}
