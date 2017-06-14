package ru.nsu.ccfit.skokova.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import java.util.ArrayList;

public class FactoryController {
    private ConfigParser configParser;
    private Storage<Engine> engineStorage;
    private Storage<Body> bodyStorage;
    private Storage<Accessory> accessoryStorage;
    private CarStorage carStorage;

    private EngineSupplier engineSupplier;
    private BodySupplier bodySupplier;
    private ArrayList<AccessorySupplier> accessorySuppliers = new ArrayList<>();
    private ArrayList<Dealer> dealers = new ArrayList<>();

    private int accessorySuppliersCount;
    private ArrayList<Thread> accessorySupplierThreads;
    private int dealersCount;
    private ArrayList<Thread> dealerThreads;
    private Thread engineSupplierThread;
    private Thread bodySupplierThread;
    private StorageController storageController;
    private Thread storageControllerThread;

    private ThreadPool threadPool;

    private Logger logger = LogManager.getLogger(FactoryController.class);

    private boolean canBeInterrupted = false;
    private boolean canBeStarted = true;

    public FactoryController(ConfigParser configParser) {
        this.configParser = configParser;
        this.engineStorage = new Storage<>(ConfigParser.map.get("EngineStorageSize"));
        this.bodyStorage = new Storage<>(ConfigParser.map.get("BodyStorageSize"));
        this.accessoryStorage = new Storage<>(ConfigParser.map.get("AccessoryStorageSize"));
        this.carStorage = new CarStorage(ConfigParser.map.get("CarStorageSize"));

        this.accessorySuppliersCount = ConfigParser.map.get("AccessorySuppliersCount");
        for (int i = 0; i < accessorySuppliersCount; i++) {
            this.accessorySuppliers.add(i, new AccessorySupplier(ConfigParser.map.get("AccessorySupplierPeriodicity"), i, accessoryStorage));
        }

        this.dealersCount = ConfigParser.map.get("DealersCount");
        for (int i = 0; i < dealersCount; i++) {
            this.dealers.add(i, new Dealer(i, ConfigParser.map.get("DealersPeriodicity"), carStorage));
        }

        this.engineSupplier = new EngineSupplier(ConfigParser.map.get("EngineSupplierPeriodicity"), engineStorage);
        this.bodySupplier = new BodySupplier(ConfigParser.map.get("BodySupplierPeriodicity"), bodyStorage);

        this.threadPool = new ThreadPool(ConfigParser.map.get("WorkersCount"), ConfigParser.map.get("TaskQueueSize"));
    }

    public void runFactory() {
        if (!configParser.isEnableLogs()) {
            Logger logger = LogManager.getRootLogger();
            Configurator.setLevel(logger.getName(), Level.OFF);
        }

        if (canBeStarted) {
            this.accessorySupplierThreads = new ArrayList<>();
            for (int i = 0; i < accessorySuppliersCount; i++) {
                Thread accessorySupplierThread = new Thread(accessorySuppliers.get(i));
                accessorySupplierThread.setName("accessorySupplier #" + i);
                this.accessorySupplierThreads.add(accessorySupplierThread);
            }

            this.dealerThreads = new ArrayList<>();
            for (int i = 0; i < dealersCount; i++) {
                Thread dealerThread = new Thread(dealers.get(i));
                dealerThread.setName("dealerThread #" + i);
                this.dealerThreads.add(dealerThread);
            }

            this.engineSupplierThread = new Thread(this.engineSupplier);
            engineSupplierThread.setName("engineSupplierThread");

            this.bodySupplierThread = new Thread(this.bodySupplier);
            bodySupplierThread.setName("bodySupplierThread");

            this.storageController = new StorageController(engineStorage, bodyStorage, accessoryStorage, carStorage, threadPool);
            this.carStorage.setStorageController(this.storageController);
            this.storageControllerThread = new Thread(storageController);
            storageControllerThread.setName("storageControllerThread");

            for (int i = 0; i < accessorySuppliersCount; i++) {
                accessorySupplierThreads.get(i).start();
            }
            for (int i = 0; i < dealersCount; i++) {
                dealerThreads.get(i).start();
            }
            engineSupplierThread.start();
            bodySupplierThread.start();
            storageControllerThread.start();
            for (Thread poolThread : threadPool.getThreads()) {
                poolThread.start();
            }
            canBeInterrupted = true;
            canBeStarted = false;
        } else {
            logger.error("Factory finished working and can't be started again");
        }
    }

    public void interruptFactory() {
        if (canBeInterrupted) {
            for (Thread poolThread : storageController.getThreadPool().getThreads()) {
                poolThread.interrupt();
            }
            storageControllerThread.interrupt();
            for (int i = 0; i < accessorySuppliersCount; i++) {
                accessorySupplierThreads.get(i).interrupt();
            }
            for (int i = 0; i < dealersCount; i++) {
                dealerThreads.get(i).interrupt();
            }
            engineSupplierThread.interrupt();
            bodySupplierThread.interrupt();
        } else {
            logger.info("Factory hasn't started, nothing to interrupt");
        }
    }

    public Storage<Engine> getEngineStorage() {
        return engineStorage;
    }

    public Storage<Body> getBodyStorage() {
        return bodyStorage;
    }

    public Storage<Accessory> getAccessoryStorage() {
        return accessoryStorage;
    }

    public CarStorage getCarStorage() {
        return carStorage;
    }

    public EngineSupplier getEngineSupplier() {
        return engineSupplier;
    }

    public BodySupplier getBodySupplier() {
        return bodySupplier;
    }

    public ArrayList<AccessorySupplier> getAccessorySuppliers() {
        return accessorySuppliers;
    }

    public ArrayList<Dealer> getDealers() {
        return dealers;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }
}
