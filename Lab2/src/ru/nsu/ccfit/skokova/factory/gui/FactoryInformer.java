package ru.nsu.ccfit.skokova.factory.gui;

import javax.swing.*;

public class FactoryInformer extends JPanel {
    private JLabel field;
    private JLabel textValue;

    public FactoryInformer(JLabel infoLabel, JLabel value) {
        this.field = infoLabel;
        this.textValue = value;

        this.add(this.field);
        this.add(this.textValue);
    }

    public class ValueChanger implements ValueChangedHandler {
        @Override
        public void handle(int value) {
            textValue.setText(Integer.toString(value));
        }
    }
}
