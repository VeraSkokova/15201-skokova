package ru.nsu.ccfit.skokova.threadpool;

import java.util.LinkedList;

public class BlockingQueue<T> {
    private int limit;
    private LinkedList<T> list;

    public BlockingQueue() {
        list = new LinkedList<>();
    }

    public BlockingQueue(int l) {
        this.limit = l;
        list = new LinkedList<>();
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

        T element = this.list.remove();
        notifyAll();
        return element;
    }

    public int getLimit() {
        return limit;
    }

    public synchronized int getSize() {
        return this.list.size();
    }

    public LinkedList<T> getList() {
        return list;
    }
}
