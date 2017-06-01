package ru.nsu.ccfit.skokova.factory;

public class BodySupplier implements Runnable {
    private int periodicity;
    private Storage<Body> storage;

    public BodySupplier(int p, Storage s) {
        this.periodicity = p;
        this.storage = s;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Body body = new Body();
                this.storage.put(body);
                System.out.println("I've put a body #" + body.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("Oops");
        }
    }
}