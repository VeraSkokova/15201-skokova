package ru.nsu.ccfit.skokova.factory;

public class Storage<T extends Detail> {
    private BlockingQueue<Detail> details;
    private int size;

    public Storage(int s) {
        this.size = s;
        this.details = new BlockingQueue<>(s);
    }

    public void put(Detail detail) throws InterruptedException {
        this.details.enqueue(detail);
        size++;
    }

    public Detail get() throws InterruptedException {
        size--;
        return details.dequeue();
    }
}
