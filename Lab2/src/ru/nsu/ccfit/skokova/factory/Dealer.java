package ru.nsu.ccfit.skokova.factory;

public class Dealer implements Runnable {
    private int id;
    private int periodicity;
    private CarStorage storage;

    public Dealer(int number, int periodicity, CarStorage storage) {
        this.id = number;
        this.periodicity = periodicity;
        this.storage = storage;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(periodicity);
                Car car = this.storage.get();
                System.out.println("Dealer # " + this.getId() + " got a car # " + car.getId() + "( body #" + car.getBody().getId() +
                                    " engine #" + car.getEngine().getId() + " accessory #" + car.getAccessory().getId() + ")");
            }
        } catch (InterruptedException e) {
            System.out.println("Oops"); //TODO
        }
    }
}
