package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.factory.gui.ValueChangedHandler;
import ru.nsu.ccfit.skokova.threadpool.BlockingQueue;

import java.util.ArrayList;

public class Storage<T extends Detail> {
    private BlockingQueue<Detail> details;
    private final int size;
    private ArrayList<ValueChangedHandler> handlers = new ArrayList<>();

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

    public Storage(int s) {
        this.size = s;
        this.details = new BlockingQueue<>(s);
    }

    public void put(Detail detail) throws InterruptedException {
        this.details.enqueue(detail);
        this.notifyValueChanged(this.details.getSize());
    }

    public Detail get() throws InterruptedException {
        Detail detail = details.dequeue();
        this.notifyValueChanged(this.details.getSize());
        return detail;
    }

    public int getSize() {
        return size;
    }
}
