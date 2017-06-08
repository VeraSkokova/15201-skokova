package ru.nsu.ccfit.skokova.factory;

public class EngineSupplier implements Runnable {
    private int periodicity;
    private Storage<Engine> storage;

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
                System.out.println("I've put an engine #" + engine.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("Caught");
            Thread.currentThread().interrupt();
        }
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Engine supplier was interrupted");
        }
    }
}
