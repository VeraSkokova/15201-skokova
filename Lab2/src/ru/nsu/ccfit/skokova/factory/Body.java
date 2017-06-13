package ru.nsu.ccfit.skokova.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class Body extends Detail{
    private static final AtomicInteger ID = new AtomicInteger(0);
    public Body() {
        this.id = ID.getAndIncrement();
    }
}
