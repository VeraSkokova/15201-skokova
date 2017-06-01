package ru.nsu.ccfit.skokova.factory;

public class AccessorySupplier implements Runnable {
    private int periodicity;
    private Storage<Accessory> storage;

    public AccessorySupplier(int p, Storage s) {
        this.periodicity = p;
        this.storage = s;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Accessory accessory = new Accessory();
                this.storage.put(accessory);
                System.out.println("I've put an accessory #" + accessory.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("Oops");
        }
    }
}
