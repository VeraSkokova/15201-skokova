package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

import java.util.ArrayList;

public class DealerUpdater implements ValueChangedHandler {
    private ArrayList<Dealer> dealers;

    public DealerUpdater(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

    @Override
    public void handle(int value) {
        for (Dealer dealer : dealers) {
            dealer.setPeriodicity(value);
        }
    }
}
