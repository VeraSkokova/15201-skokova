package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.BlockingQueue;

public class CarStorage {
    private BlockingQueue<Car> cars;
    private int size;

    public CarStorage(int s) {
        this.size = s;
        this.cars = new BlockingQueue<>(s);
    }

    public void put(Car car) throws InterruptedException {
        this.cars.enqueue(car);
    }

    public Car get() throws InterruptedException {
        return this.cars.dequeue();
    }

    public BlockingQueue<Car> getCars() {
        return cars;
    }

    public int getSize() {
        return size;
    }
}
