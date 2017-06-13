package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

public class StorageController implements Runnable{
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;
    private ThreadPool threadPool;

    private Logger logger = LogManager.getLogger(StorageController.class);

    public StorageController(Storage<Engine> engineStorage, Storage<Body> bodyStorage, Storage<Accessory> accessoryStorage, CarStorage carStorage, ThreadPool threadPool) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.threadPool = threadPool;
    }

    private void makeCar() throws InterruptedException {
        this.threadPool.addTask(new Worker(this.engineStorage, this.bodyStorage, this.accessoryStorage, this.carStorage));
    }

    private synchronized void makeNewCars() throws InterruptedException{
        int count = this.carStorage.getSize() - this.carStorage.getCars().getSize();
        for (int i = 0; i < count; i++) {
            this.makeCar();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.makeNewCars();
            }
        } catch (InterruptedException e) {
            logger.warn("StorageController was interrupted");
        }
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }
}
