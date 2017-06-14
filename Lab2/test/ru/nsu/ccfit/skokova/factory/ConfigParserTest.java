package ru.nsu.ccfit.skokova.factory;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ConfigParserTest {
  String configFileName = "test/ru/nsu/ccfit/skokova/factory/Config.txt";

  @Test
  public void test() throws IOException, BadParseException {
    ConfigParser configParser = new ConfigParser(configFileName);
    Assert.assertEquals(Integer.valueOf(100), ConfigParser.getMap().get("EngineStorageSize"));
    Assert.assertEquals(Integer.valueOf(150), ConfigParser.getMap().get("BodyStorageSize"));
    Assert.assertEquals(Integer.valueOf(200), ConfigParser.getMap().get("AccessoryStorageSize"));
    Assert.assertEquals(Integer.valueOf(100), ConfigParser.getMap().get("CarStorageSize"));
    Assert.assertEquals(Integer.valueOf(1), ConfigParser.getMap().get("AccessorySuppliersCount"));
    Assert.assertEquals(Integer.valueOf(25), ConfigParser.getMap().get("WorkersCount"));
    Assert.assertEquals(Integer.valueOf(1), ConfigParser.getMap().get("DealersCount"));
    Assert.assertEquals(Integer.valueOf(100), ConfigParser.getMap().get("EngineSupplierPeriodicity"));
    Assert.assertEquals(Integer.valueOf(150), ConfigParser.getMap().get("BodySupplierPeriodicity"));
    Assert.assertEquals(Integer.valueOf(200), ConfigParser.getMap().get("AccessorySupplierPeriodicity"));
    Assert.assertEquals(Integer.valueOf(250), ConfigParser.getMap().get("DealersPeriodicity"));
    Assert.assertEquals(Integer.valueOf(250), ConfigParser.getMap().get("TaskQueueSize"));
    Assert.assertEquals(true, configParser.isEnableLogs());

    System.out.println("TEST ConfigParserTest PASSED");
  }
}
