package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccessorySupplier implements Runnable {
    private int periodicity;
    private int id;
    private Storage<Accessory> storage;

    private static final Logger logger = LogManager.getLogger(AccessorySupplier.class);

    public AccessorySupplier(int periodicity, int id, Storage<Accessory> storage) {
        this.periodicity = periodicity;
        this.id = id;
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
                Accessory accessory = new Accessory();
                this.storage.put(accessory);
            }
        } catch (InterruptedException e) {
            logger.warn("AccessorySupplier #" + this.getId() + " was interrupted");
        }
    }
}
