package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Dealer implements Runnable {
    private int id;
    private int periodicity;
    private CarStorage storage;

    private static int carsSold;
    private static final AtomicInteger CAR_COUNTER = new AtomicInteger(1);

    private static ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(Dealer.class);

    public Dealer(int number, int periodicity, CarStorage storage) {
        this.id = number;
        this.periodicity = periodicity;
        this.storage = storage;
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

    public int getId() {
        return id;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
        logger.info("Dealer #" + this.getId() + " changed periodicity to " + periodicity);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Car car = this.storage.get();
                Dealer.carsSold = Dealer.CAR_COUNTER.getAndIncrement();
                this.notifyValueChanged(Dealer.carsSold);
                logger.info("Dealer #" + this.getId() + " got a car #" + car.getId() + "(body #" + car.getBody().getId() +
                        " engine #" + car.getEngine().getId() + " accessory #" + car.getAccessory().getId() + ")");
            }
        } catch (InterruptedException e) {
            logger.warn("Dealer #" + this.getId() + " was interrupted");
        }
    }
}
