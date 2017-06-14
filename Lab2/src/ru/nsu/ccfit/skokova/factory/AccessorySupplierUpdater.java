package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

import java.util.ArrayList;

public class AccessorySupplierUpdater implements ValueChangedHandler {
    private ArrayList<AccessorySupplier> accessorySuppliers;

    public AccessorySupplierUpdater(ArrayList<AccessorySupplier> accessorySuppliers) {
        this.accessorySuppliers = accessorySuppliers;
    }

    @Override
    public void handle(int value) {
        for (AccessorySupplier accessorySupplier : accessorySuppliers) {
            accessorySupplier.setPeriodicity(value);
        }
    }
}
