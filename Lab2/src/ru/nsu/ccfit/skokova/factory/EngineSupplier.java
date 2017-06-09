package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EngineSupplier implements Runnable {
    private int periodicity;
    private Storage<Engine> storage;

    private static final Logger logger = LogManager.getLogger(EngineSupplier.class);

    public EngineSupplier(int periodicity, Storage<Engine> storage) {
        this.periodicity = periodicity;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Engine engine = new Engine();
                this.storage.put(engine);
            }
        } catch (InterruptedException e) {
            logger.warn("EngineSupplier was interrupted");
        }
    }
}
