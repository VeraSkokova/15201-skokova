package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

public class StorageController implements Runnable{
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;
    private ThreadPool threadPool;

    public StorageController(Storage<Engine> engineStorage, Storage<Body> bodyStorage, Storage<Accessory> accessoryStorage, CarStorage carStorage, ThreadPool threadPool) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.threadPool = threadPool;
    }

    private void makeCar() {
        this.threadPool.addTask(new Worker(this.engineStorage, this.bodyStorage, this.accessoryStorage, this.carStorage));
    }

    private synchronized void makeNewCars() {
        int count = this.carStorage.getSize() - this.carStorage.getCars().getSize();
        for (int i = 0; i < count; i++) {
            this.makeCar();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            this.makeNewCars();
        }
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }
}
