package ru.nsu.ccfit.skokova.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;

import java.util.ArrayList;

public class ThreadPool {
    private int size;
    private BlockingQueue<Runnable> taskQueue;
    private ArrayList<Thread> threads;

    private ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(ThreadPool.class);

    public ThreadPool(int size, int queueSize) {
        this.size = size;
        taskQueue = new BlockingQueue<>(queueSize);
        threads = new ArrayList<>();
        for (int i = 0; i < this.size; i++) {
            Thread temp = new Thread(new PoolThreadRunnable(taskQueue, this));
            temp.setName("Worker #" + i);
            threads.add(temp);
        }
    }

    public void addHandler(ValueChangedHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
    }

    public void notifyValueChanged(int value) {
        for (ValueChangedHandler handler : handlers) {
            handler.handle(value);
        }
    }

    public synchronized void addTask(Runnable task) throws InterruptedException {
        this.taskQueue.enqueue(task);
        this.notifyValueChanged(this.taskQueue.getSize());
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

    public BlockingQueue<Runnable> getTaskQueue() {
        return taskQueue;
    }
}
