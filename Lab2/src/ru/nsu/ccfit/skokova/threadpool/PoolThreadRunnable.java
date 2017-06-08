package ru.nsu.ccfit.skokova.threadpool;

public class PoolThreadRunnable implements Runnable {
    private BlockingQueue<Runnable> taskQueue;
    private boolean isStopped;

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
            System.out.println("Oops"); //TODO
        }
    }

    public synchronized void stopTask() {
        this.isStopped = true;
    }

    public synchronized boolean isStopped() {
        return this.isStopped;
    }
}
