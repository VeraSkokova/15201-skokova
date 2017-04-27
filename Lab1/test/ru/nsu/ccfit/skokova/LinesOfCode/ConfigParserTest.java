package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigParserTest {
  String configName = "test/ru/nsu/ccfit/skokova/LinesOfCode/TestDirs/Testing/config.txt";

  @Test
  public void test() throws IOException {
    try {
      Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.ExtentionFilterSerializer");
      Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.EarlierTimeFilterSerializer");
      Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.LaterTimeFilterSerializer");
      Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.AndFilterSerializer");
      Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.OrFilterSerializer");
      Class.forName("ru.nsu.ccfit.skokova.LinesOfCode.NotFilterSerializer");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    ConfigParser configParser = new ConfigParser(configName);
    Assert.assertEquals(".h", configParser.getFilterStrings().get(0));
    Assert.assertEquals("     .c", configParser.getFilterStrings().get(1));
    Assert.assertEquals(">1400000000", configParser.getFilterStrings().get(2));
    Assert.assertEquals(".cpp", configParser.getFilterStrings().get(3));
    Assert.assertEquals("\t\t.java", configParser.getFilterStrings().get(4));
    System.out.println("TEST ConfigParserTest PASSED");
  }
}
