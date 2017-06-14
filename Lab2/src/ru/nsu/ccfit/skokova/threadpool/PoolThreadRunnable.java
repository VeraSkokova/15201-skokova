package ru.nsu.ccfit.skokova.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoolThreadRunnable implements Runnable {
    private BlockingQueue<Runnable> taskQueue;
    private ThreadPool threadPool;

    private static final Logger logger = LogManager.getLogger(PoolThreadRunnable.class);

    public PoolThreadRunnable(BlockingQueue<Runnable> taskQueue, ThreadPool threadPool) {
        this.taskQueue = taskQueue;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                taskQueue.dequeue().run();
                this.threadPool.notifyValueChanged(taskQueue.getSize());
            }
        } catch (InterruptedException e) {
            logger.warn("Worker was interrupted");
        }
    }
}
