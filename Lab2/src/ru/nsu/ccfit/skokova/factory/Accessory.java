package ru.nsu.ccfit.skokova.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class Accessory extends Detail{
    private static final AtomicInteger ID = new AtomicInteger(0);
    public Accessory() {
        this.id = ID.getAndIncrement();
    }
}
