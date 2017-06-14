package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BodySupplier implements Runnable {
    private int periodicity;
    private Storage<Body> storage;

    private static final Logger logger = LogManager.getLogger(BodySupplier.class);

    public BodySupplier(int periodicity, Storage<Body> storage) {
        this.periodicity = periodicity;
        this.storage = storage;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
        logger.info("BodySupplier changed periodicity to " + periodicity);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Body body = new Body();
                this.storage.put(body);
                //logger.debug("BodySupplier put a body #" + body.getId());
            }
        } catch (InterruptedException e) {
            logger.warn("BodySupplier was interrupted");
        }
    }
}
