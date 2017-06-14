package ru.nsu.ccfit.skokova.factory.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 5000;

    private static final Logger logger = LogManager.getLogger(FactoryRegulator.class);

    public FactoryRegulator(JLabel label, int min, int max, String textField) {
        this.label = label;
        this.textField = new JTextField(textField);

        this.slider = new JSlider(min, max);
        slider.setMinorTickSpacing(min);
        slider.setMajorTickSpacing(max);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        this.slider.setValue(Integer.parseInt(textField));
        this.slider.addChangeListener(new SliderListener());


        this.textField.setPreferredSize(new Dimension(125, 25));
        this.textField.addActionListener(new TextFieldListener());

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
        }
    }


    class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            int value = slider.getValue();
            textField.setText(Integer.toString(value));
            notifyValueChanged(value);
        }
    }

    class TextFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                int value = Integer.parseInt(textField.getText());
                if ((value < MIN_VALUE) || (value > MAX_VALUE)) {
                    throw new NumberFormatException("Incorrect value");
                }
                slider.setValue(value);
                notifyValueChanged(value);
            } catch (NumberFormatException e) {
                textField.setForeground(Color.RED);
                logger.error("Wrong field value");
            }
        }
    }
}
