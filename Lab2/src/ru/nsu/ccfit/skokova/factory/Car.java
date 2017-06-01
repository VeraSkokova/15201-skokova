package ru.nsu.ccfit.skokova.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {
    private static final AtomicInteger ID = new AtomicInteger(0);
    private final int id;
    private Engine engine;
    private Body body;
    private Accessory accessory;

    public Car(Engine e, Body b, Accessory a) {
        this.id = ID.getAndIncrement();
        this.engine = e;
        this.body = b;
        this.accessory = a;
    }

    public int getId() {
        return id;
    }

    public Engine getEngine() {
        return engine;
    }

    public Body getBody() {
        return body;
    }

    public Accessory getAccessory() {
        return accessory;
    }
}
