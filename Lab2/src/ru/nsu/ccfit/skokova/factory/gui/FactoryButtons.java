package ru.nsu.ccfit.skokova.factory.gui;

import javax.swing.*;

public class FactoryButtons extends JPanel {
    private JButton runButton;
    private JButton stopButton;

    public FactoryButtons() {
        this.runButton = new JButton("Run");
        this.stopButton = new JButton("Stop");

        this.add(this.runButton);
        this.add(this.stopButton);
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }
}
