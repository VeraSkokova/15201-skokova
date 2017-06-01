package ru.nsu.ccfit.skokova.factory;

import java.util.ArrayList;

public class BlockingQueue<T> {
    private int limit;
    private ArrayList<T> list;

    public BlockingQueue() {
        list = new ArrayList<T>();
    }

    public BlockingQueue(int l) {
        this.limit = l;
        list = new ArrayList<>(l);
    }

    public synchronized void enqueue(T element) throws InterruptedException {
        while (this.list.size() == this.limit) {
            wait();
        }
        list.add(element);
        notifyAll();
    }


    public synchronized T dequeue() throws InterruptedException {
        while (this.list.size() == 0) {
            wait();
        }

        T element = this.list.get(0);
        this.list.remove(0);
        notifyAll();
        return element;
    }
}
