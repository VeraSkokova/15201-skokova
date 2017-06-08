package ru.nsu.ccfit.skokova.factory;

public class Worker implements Runnable{
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    public Worker(Storage<Engine> engineStorage, Storage<Body> bodyStorage, Storage<Accessory> accessoryStorage, CarStorage carStorage) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Engine engine = (Engine) engineStorage.get();
                Body body = (Body) bodyStorage.get();
                Accessory accessory = (Accessory) accessoryStorage.get();
                Car car = new Car(engine, body, accessory);
                carStorage.put(car);
                System.out.println("Worker made a car #" + car.getId() + "( body #" + car.getBody().getId() +
                        " engine #" + car.getEngine().getId() + " accessory #" + car.getAccessory().getId() + ")");
            }
        } catch (InterruptedException e) {
            System.out.println("Oops"); //TODO
        }
    }
}
