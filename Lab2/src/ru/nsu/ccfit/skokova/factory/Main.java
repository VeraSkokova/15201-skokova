package ru.nsu.ccfit.skokova.factory;

import ru.nsu.ccfit.skokova.threadpool.ThreadPool;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            String name = "/home/veraskokova/Документы/MyConfig.txt";
            ConfigParser configParser= new ConfigParser(name);
            Storage<Engine> engineStorage = new Storage<>(ConfigParser.getConfigPairs().get(0).getValue());
            Storage<Body> bodyStorage = new Storage<>(ConfigParser.getConfigPairs().get(1).getValue());
            Storage<Accessory> accessoryStorage = new Storage<>(ConfigParser.getConfigPairs().get(2).getValue());
            CarStorage carStorage = new CarStorage(ConfigParser.getConfigPairs().get(3).getValue());
            int accessorySuppliersCount = ConfigParser.getConfigPairs().get(4).getValue();
            ArrayList<Thread> accessorySupplierThreads = new ArrayList<>();
            for (int i = 0; i < accessorySuppliersCount; i++) {
                accessorySupplierThreads.add(new Thread(new AccessorySupplier(ConfigParser.getConfigPairs().get(9).getValue(), accessoryStorage)));
            }
            int dealersCount = ConfigParser.getConfigPairs().get(6).getValue();
            ArrayList<Thread> dealerThreads = new ArrayList<>();
            for (int i = 0; i < dealersCount; i++) {
                dealerThreads.add(new Thread(new Dealer(i, ConfigParser.getConfigPairs().get(10).getValue(), carStorage)));
            }
            Thread engineSupplierThread = new Thread(new EngineSupplier(ConfigParser.getConfigPairs().get(7).getValue(), engineStorage));
            Thread bodySupplierThread = new Thread(new BodySupplier(ConfigParser.getConfigPairs().get(8).getValue(), bodyStorage));
            ThreadPool threadPool = new ThreadPool(ConfigParser.getConfigPairs().get(5).getValue(), ConfigParser.getConfigPairs().get(11).getValue());
            Controller controller = new Controller(engineStorage, bodyStorage, accessoryStorage, carStorage, threadPool);
            Thread controllerThread = new Thread(controller);

            for (int i = 0; i < accessorySuppliersCount; i++) {
                accessorySupplierThreads.get(i).start();
            }
            for (int i = 0; i < dealersCount; i++) {
                dealerThreads.get(i).start();
            }
            engineSupplierThread.start();
            bodySupplierThread.start();
            controllerThread.start();
            for (Thread poolThread : controller.getThreadPool().getThreads()) {
                poolThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
