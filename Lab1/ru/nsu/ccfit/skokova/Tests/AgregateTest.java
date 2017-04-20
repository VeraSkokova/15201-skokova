package ru.nsu.ccfit.skokova.LinesOfCode;

import static ru.nsu.ccfit.skokova.LinesOfCode.Controller;

import java.io.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AgregateTest {
  String configName = "./Testing1/config.txt";
  String dirName = "./Testing1/";

  @Test
  public void test() throws IOException {
    Controller controller = new Controller(dirName, configName);
    controller.fillStatistics();
    Assert.assertEquals(1, controller.getStatistics().getStat().get("|(.h .cpp)").getFilesCount());
    Assert.assertEquals(1, controller.getStatistics().getStat().get("|(.h .cpp)").getFilesCount());
    Assert.assertEquals(15, controller.getStatistics().getStat().get("&(.c >1400000000)").getLinesCount());
    Assert.assertEquals(5, controller.getStatistics().getStat().get("&(.c >1400000000)").getFilesCount());
    System.out.println("TEST AgregaTest PASSED");
  }
}
