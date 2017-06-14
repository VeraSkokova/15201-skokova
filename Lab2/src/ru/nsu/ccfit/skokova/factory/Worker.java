package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable{
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    private static int carsMade;
    private static final AtomicInteger CAR_COUNTER = new AtomicInteger(1);

    private static ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(Worker.class);

    public Worker(Storage<Engine> engineStorage, Storage<Body> bodyStorage, Storage<Accessory> accessoryStorage, CarStorage carStorage) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    public static void addHandler(ValueChangedHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
    }

    public void notifyValueChanged(int value) {
        for (ValueChangedHandler handler : handlers) {
            handler.handle(value);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Engine engine = (Engine) engineStorage.get();
                Body body = (Body) bodyStorage.get();
                Accessory accessory = (Accessory) accessoryStorage.get();
                //logger.debug(Thread.currentThread().getName() + "got an engine #" + engine.getId());
                //logger.debug(Thread.currentThread().getName() + "got a body #" + body.getId());
                //logger.debug(Thread.currentThread().getName() + "got an accessory #" + accessory.getId());
                Car car = new Car(engine, body, accessory);
                carStorage.put(car);
                Worker.carsMade = Worker.CAR_COUNTER.getAndIncrement();
                this.notifyValueChanged(Worker.carsMade);
                //logger.debug(Thread.currentThread().getName() + "put a car#" + car.getId());
            }
        } catch (InterruptedException e) {
            logger.warn( Thread.currentThread().getName() + " was interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
