package ru.nsu.ccfit.skokova.factory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Detail {
    private static final AtomicInteger ID = new AtomicInteger(0);
    private final int id;


    public Detail() {
        this.id = ID.getAndIncrement();
    }

    public int getId() {
        return id;
    }
}

