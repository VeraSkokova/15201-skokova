package ru.nsu.ccfit.skokova.factory;

public class AccessorySupplier implements Runnable {
    private int periodicity;
    private Storage<Accessory> storage;

    public AccessorySupplier(int periodicity, Storage<Accessory> storage) {
        this.periodicity = periodicity;
        this.storage = storage;
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
            System.out.println("Oops"); //TODO
        }
    }
}
