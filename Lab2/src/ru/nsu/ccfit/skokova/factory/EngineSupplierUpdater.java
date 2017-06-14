package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

public class EngineSupplierUpdater implements ValueChangedHandler {
    private EngineSupplier engineSupplier;

    public EngineSupplierUpdater(EngineSupplier engineSupplier) {
        this.engineSupplier = engineSupplier;
    }

    @Override
    public void handle(int value) {
        this.engineSupplier.setPeriodicity(value);
    }
}
