package ru.nsu.ccfit.skokova.factory;

import java.util.ArrayList;

public class BlockingQueue {
    private int limit;
    private ArrayList<Object> list;

    public BlockingQueue(int l) {
        this.limit = l;
        list = new ArrayList<>(l);
    }

    public synchronized void enqueue(Object o) throws InterruptedException{
        while (this.list.size() == this.limit) {
            wait();
        }
        if (this.list.size() != this.limit) {
            notifyAll();
        }
        list.add(o);
    }


    public synchronized Object dequeue() throws InterruptedException {
        while (this.list.size() == 0) {
            wait();
        }
        if (this.list.size() == this.limit) {
            notifyAll();
        }
        Object o = this.list.get(0);
        this.list.remove(0);
        return o;
    }
}
