package ru.nsu.ccfit.skokova.threadpool;

import java.util.ArrayList;

public class ThreadPool {
    private int size;
    private BlockingQueue<Runnable> taskQueue;
    private ArrayList<Thread> threads;

    public ThreadPool(int size, int queueSize) {
        this.size = size;
        taskQueue = new BlockingQueue<>(queueSize);
        threads = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Thread temp = new Thread(new PoolThreadRunnable(taskQueue));
            temp.setName("Worker");
            threads.add(temp);
        }
    }

    public synchronized void addTask(Runnable task) {
        try {
            this.taskQueue.enqueue(task);
        } catch (InterruptedException e) {
            System.out.println("Oops"); //TODO
        }
    }

    public synchronized void interrupt() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }
}
