package ru.nsu.ccfit.skokova.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoolThreadRunnable implements Runnable {
    private BlockingQueue<Runnable> taskQueue;
    private boolean isStopped;

    private static final Logger logger = LogManager.getLogger(PoolThreadRunnable.class);

    public PoolThreadRunnable(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
        this.isStopped = false;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                taskQueue.dequeue().run();
            }
        } catch (InterruptedException e) {
            logger.warn("Worker was interrupted");
        }
    }

    public synchronized void stopTask() {
        this.isStopped = true;
    }

    public synchronized boolean isStopped() {
        return this.isStopped;
    }
}
