package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Worker implements Runnable{
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    private static final Logger logger = LogManager.getLogger(Worker.class);

    public Worker(Storage<Engine> engineStorage, Storage<Body> bodyStorage, Storage<Accessory> accessoryStorage, CarStorage carStorage) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Engine engine = (Engine) engineStorage.get();
                Body body = (Body) bodyStorage.get();
                Accessory accessory = (Accessory) accessoryStorage.get();
                Car car = new Car(engine, body, accessory);
                carStorage.put(car);
            }
        } catch (InterruptedException e) {
            logger.warn("Worker was interrupted");
        }
    }
}
