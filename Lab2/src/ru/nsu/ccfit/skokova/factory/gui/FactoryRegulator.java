package ru.nsu.ccfit.skokova.factory.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FactoryRegulator extends JPanel {
    private JSlider slider;
    private JTextField textField;
    private JLabel label;
    private ArrayList<ValueChangedHandler> handlers;

    public FactoryRegulator(JLabel label, int min, int max) {
        this.handlers = new ArrayList<>();
        this.label = label;

        this.slider = new JSlider(min, max);
        slider.setMinorTickSpacing(min);
        slider.setMajorTickSpacing(max);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        this.slider.addChangeListener(new SliderListener());

        this.textField = new JTextField();
        textField.setPreferredSize(new Dimension(125, 25));
        textField.addActionListener(new TextFieldListener());

        this.add(this.label);
        this.add(this.slider);
        this.add(this.textField);
    }

    private void notifyValueChanged(int value) {
        for (ValueChangedHandler handler : handlers) {
            handler.handle(value);
        }
    }

    public void addHandler(ValueChangedHandler handler) {
        if (handler != null) {
            this.handlers.add(handler);
        } //TODO else?
    }


    class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            int value = slider.getValue();
            textField.setText(Integer.toString(value));
            notifyValueChanged(value);
            //System.out.println("Changed");
        }
    }

    class TextFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int value = Integer.parseInt(textField.getText());
            slider.setValue(value);
            notifyValueChanged(value);
            //System.out.println("Changed");
        }
    }
}
