package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigParserTest {
  String configName = "./Testing/config.txt";

  @Test
  public void test() {
    ConfigParser configParser = new ConfigParser(configName);
    Assert.assertEquals(".h", configParser.getFilterStrings().get(0));
    Assert.assertEquals("     .c", configParser.getFilterStrings().get(1));
    Assert.assertEquals(">1400000000", configParser.getFilterStrings().get(2));
    Assert.assertEquals(".cpp", configParser.getFilterStrings().get(3));
    Assert.assertEquals("   .java", configParser.getFilterStrings().get(4));
    System.out.println("TEST ConfigParserTest PASSED");
  }
}
