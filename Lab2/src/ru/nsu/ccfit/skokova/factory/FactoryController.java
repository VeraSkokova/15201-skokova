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

    private boolean canBeInterrupted = false;

    public FactoryController(ConfigParser configParser) {
        this.configParser = configParser;
        this.engineStorage = new Storage<>(ConfigParser.getConfigPairs().get(ConfigParser.ENGINE_STORAGE_SIZE).getValue());
        this.bodyStorage = new Storage<>(ConfigParser.getConfigPairs().get(ConfigParser.BODY_STORAGE_SIZE).getValue());
        this.accessoryStorage = new Storage<>(ConfigParser.getConfigPairs().get(ConfigParser.ACCESSORY_STORAGE_SIZE).getValue());
        this.carStorage = new CarStorage(ConfigParser.getConfigPairs().get(ConfigParser.CAR_STORAGE_SIZE).getValue());
    }

    public void runFactory() {
        if (!configParser.isEnableLogs()) {
            Logger logger = LogManager.getRootLogger();
            Configurator.setLevel(logger.getName(), Level.OFF);
        }
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CreateAndShowGUI.createAndShowGUI();
            }
        });*/

        this.accessorySuppliersCount = ConfigParser.getConfigPairs().get(ConfigParser.ACCESSORY_SUPPLIERS_COUNT).getValue();
        this.accessorySupplierThreads = new ArrayList<>();
        for (int i = 0; i < accessorySuppliersCount; i++) {
            this.accessorySuppliers.add(i, new AccessorySupplier(ConfigParser.getConfigPairs().get(ConfigParser.ACCESSORY_SUPPLIER_PERIODICITY).getValue(), i, accessoryStorage));
            Thread accessorySupplierThread = new Thread(accessorySuppliers.get(i));
            accessorySupplierThread.setName("accessorySupplier #" + i);
            this.accessorySupplierThreads.add(accessorySupplierThread);
        }
        this.dealersCount = ConfigParser.getConfigPairs().get(ConfigParser.DEALERS_COUNT).getValue();
        this.dealerThreads = new ArrayList<>();
        for (int i = 0; i < dealersCount; i++) {
            this.dealers.add(i, new Dealer(i, ConfigParser.getConfigPairs().get(ConfigParser.DEALERS_PERIODICITY).getValue(), carStorage));
            Thread dealerThread = new Thread(dealers.get(i));
            dealerThread.setName("dealerThread #" + i);
            this.dealerThreads.add(dealerThread);
        }

        this.engineSupplier = new EngineSupplier(ConfigParser.getConfigPairs().get(ConfigParser.ENGINE_SUPPLIER_PERIODICITY).getValue(), engineStorage);
        this.engineSupplierThread = new Thread(this.engineSupplier);
        engineSupplierThread.setName("engineSupplierThread");
        this.bodySupplier = new BodySupplier(ConfigParser.getConfigPairs().get(ConfigParser.BODY_SUPPLIER_PERIODICITY).getValue(), bodyStorage);
        this.bodySupplierThread = new Thread(this.bodySupplier);
        bodySupplierThread.setName("bodySupplierThread");
        ThreadPool threadPool = new ThreadPool(ConfigParser.getConfigPairs().get(ConfigParser.WORKERS_SUPPLIERS_COUNT).getValue(), ConfigParser.getConfigPairs().get(ConfigParser.TASK_QUEUE_SIZE).getValue());
        this.storageController = new StorageController(engineStorage, bodyStorage, accessoryStorage, carStorage, threadPool);
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
        for (Thread poolThread : storageController.getThreadPool().getThreads()) {
            poolThread.start();
        }
        canBeInterrupted = true;
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
}
