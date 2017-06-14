package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;
import ru.nsu.ccfit.skokova.threadpool.BlockingQueue;

import java.util.ArrayList;

public class CarStorage {
    private BlockingQueue<Car> cars;
    private final int size;
    private ArrayList<ValueChangedHandler> handlers = new ArrayList<>();
    private StorageController storageController;

    public void addHandler(ValueChangedHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
    }

    public void notifyValueChanged(int value) {
        for (ValueChangedHandler handler : handlers) {
            handler.handle(value);
        }
    }

    public CarStorage(int s) {
        this.size = s;
        this.cars = new BlockingQueue<>(s);
    }

    public void put(Car car) throws InterruptedException {
        this.cars.enqueue(car);
        this.notifyValueChanged(this.cars.getSize());
    }

    public Car get() throws InterruptedException {
        Car car =  this.cars.dequeue();
        this.storageController.notifyStorageController();
        this.notifyValueChanged(this.cars.getSize());
        return car;
    }

    public BlockingQueue<Car> getCars() {
        return cars;
    }

    public int getSize() {
        return size;
    }

    public void setStorageController(StorageController storageController) {
        this.storageController = storageController;
    }
}
