package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import java.util.ArrayList;

public class FactoryController {
    private ConfigParser configParser;
    private int accessorySuppliersCount;
    private ArrayList<Thread> accessorySupplierThreads;
    private int dealersCount;
    private ArrayList<Thread> dealerThreads;
    private Thread engineSupplierThread;
    private Thread bodySupplierThread;
    private StorageController storageController;
    private Thread storageControllerThread;

    public FactoryController(ConfigParser configParser) {
        this.configParser = configParser;
    }

    public void runFactory() {
        if (!configParser.isEnableLogs()) {
            Logger logger = LogManager.getRootLogger();
            Configurator.setLevel(logger.getName(), Level.OFF);
        }
        Storage<Engine> engineStorage = new Storage<>(ConfigParser.getConfigPairs().get(ConfigParser.ENGINE_STORAGE_SIZE).getValue());
        Storage<Body> bodyStorage = new Storage<>(ConfigParser.getConfigPairs().get(ConfigParser.BODY_STORAGE_SIZE).getValue());
        Storage<Accessory> accessoryStorage = new Storage<>(ConfigParser.getConfigPairs().get(ConfigParser.ACCESSORY_STORAGE_SIZE).getValue());
        CarStorage carStorage = new CarStorage(ConfigParser.getConfigPairs().get(ConfigParser.CAR_STORAGE_SIZE).getValue());
        this.accessorySuppliersCount = ConfigParser.getConfigPairs().get(ConfigParser.ACCESSORY_SUPPLIERS_COUNT).getValue();
        this.accessorySupplierThreads = new ArrayList<>();
        for (int i = 0; i < accessorySuppliersCount; i++) {
            this.accessorySupplierThreads.add(new Thread(new AccessorySupplier(ConfigParser.getConfigPairs().get(ConfigParser.ACCESSORY_SUPPLIER_PERIODICITY).getValue(), i, accessoryStorage)));
        }
        this.dealersCount = ConfigParser.getConfigPairs().get(ConfigParser.DEALERS_COUNT).getValue();
        this.dealerThreads = new ArrayList<>();
        for (int i = 0; i < dealersCount; i++) {
            this.dealerThreads.add(new Thread(new Dealer(i, ConfigParser.getConfigPairs().get(ConfigParser.DEALERS_PERIODICITY).getValue(), carStorage)));
        }

        this.engineSupplierThread = new Thread(new EngineSupplier(ConfigParser.getConfigPairs().get(ConfigParser.ENGINE_SUPPLIER_PERIODICITY).getValue(), engineStorage));
        this.bodySupplierThread = new Thread(new BodySupplier(ConfigParser.getConfigPairs().get(ConfigParser.BODY_SUPPLIER_PERIODICITY).getValue(), bodyStorage));
        ThreadPool threadPool = new ThreadPool(ConfigParser.getConfigPairs().get(ConfigParser.WORKERS_SUPPLIERS_COUNT).getValue(), ConfigParser.getConfigPairs().get(ConfigParser.TASK_QUEUE_SIZE).getValue());
        this.storageController = new StorageController(engineStorage, bodyStorage, accessoryStorage, carStorage, threadPool);
        this.storageControllerThread = new Thread(storageController);

        for (int i = 0; i < accessorySuppliersCount; i++) {
            accessorySupplierThreads.get(i).start();
        }
        for (int i = 0; i < dealersCount; i++) {
            dealerThreads.get(i).start();
        }
        engineSupplierThread.start();
        bodySupplierThread.start();
        storageControllerThread.start();
        for (Thread poolThread : storageController.getThreadPool().getThreads()) {
            poolThread.start();
        }
    }

    public void interruptFactory() {
        for (int i = 0; i < accessorySuppliersCount; i++) {
            accessorySupplierThreads.get(i).interrupt();
        }
        for (int i = 0; i < dealersCount; i++) {
            dealerThreads.get(i).interrupt();
        }
        engineSupplierThread.interrupt();
        bodySupplierThread.interrupt();
        storageControllerThread.interrupt();
        for (Thread poolThread : storageController.getThreadPool().getThreads()) {
            poolThread.interrupt();
        }
    }
}
