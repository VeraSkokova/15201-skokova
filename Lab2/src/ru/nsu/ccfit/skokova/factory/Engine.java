package ru.nsu.ccfit.skokova.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine extends Detail{
    private static final AtomicInteger ID = new AtomicInteger(0);
    public Engine() {
        this.id = ID.getAndIncrement();
    }

}
