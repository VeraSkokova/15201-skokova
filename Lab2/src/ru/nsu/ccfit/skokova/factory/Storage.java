package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.BlockingQueue;

public class Storage<T extends Detail> {
    private BlockingQueue<Detail> details;
    private int count;
    private final int size;

    public Storage(int s) {
        this.size = s;
        this.count = 0;
        this.details = new BlockingQueue<>(s);
    }

    public void put(Detail detail) throws InterruptedException {
        this.details.enqueue(detail);
        count++;
    }

    public Detail get() throws InterruptedException {
        count--;
        return details.dequeue();
    }
}
