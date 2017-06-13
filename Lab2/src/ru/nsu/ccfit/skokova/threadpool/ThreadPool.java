package ru.nsu.ccfit.skokova.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ThreadPool {
    private int size;
    private BlockingQueue<Runnable> taskQueue;
    private ArrayList<Thread> threads;

    private static final Logger logger = LogManager.getLogger(ThreadPool.class);

    public ThreadPool(int size, int queueSize) {
        this.size = size;
        taskQueue = new BlockingQueue<>(queueSize);
        threads = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Thread temp = new Thread(new PoolThreadRunnable(taskQueue));
            temp.setName("Worker #" + i);
            threads.add(temp);
        }
    }

    public synchronized void addTask(Runnable task) throws InterruptedException {
        this.taskQueue.enqueue(task);
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
