package ru.nsu.ccfit.skokova.factory;

public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine();
        Body body = new Body();
        Accessory accessory = new Accessory();
        System.out.println(engine.getId());
        System.out.println(body.getId());
        System.out.println(accessory.getId());
        Storage<Engine> engineStorage = new Storage<>(10);
        EngineSupplier engineSupplier = new EngineSupplier(10, engineStorage);
        Storage<Body> bodyStorage = new Storage<>(7);
        BodySupplier bodySupplier = new BodySupplier(7, bodyStorage);
        Thread engineThread = new Thread(engineSupplier);
        Thread bodyThread = new Thread(bodySupplier);
        engineThread.start();
        bodyThread.start();
    }
}
