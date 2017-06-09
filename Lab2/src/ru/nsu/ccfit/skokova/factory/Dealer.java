package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dealer implements Runnable {
    private int id;
    private int periodicity;
    private CarStorage storage;

    private static final Logger logger = LogManager.getLogger(Dealer.class);

    public Dealer(int number, int periodicity, CarStorage storage) {
        this.id = number;
        this.periodicity = periodicity;
        this.storage = storage;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Car car = this.storage.get();
                logger.info("Dealer # " + this.getId() + " got a car # " + car.getId() + "( body #" + car.getBody().getId() +
                        " engine #" + car.getEngine().getId() + " accessory #" + car.getAccessory().getId() + ")");
            }
        } catch (InterruptedException e) {
            logger.warn("Dealer #" + this.getId() + " was interrupted");
        }
    }
}
