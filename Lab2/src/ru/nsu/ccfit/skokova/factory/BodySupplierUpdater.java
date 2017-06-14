package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

public class BodySupplierUpdater implements ValueChangedHandler {
    private BodySupplier bodySupplier;

    public BodySupplierUpdater(BodySupplier bodySupplier) {
        this.bodySupplier = bodySupplier;
    }

    @Override
    public void handle(int value) {
        this.bodySupplier.setPeriodicity(value);
    }
}
