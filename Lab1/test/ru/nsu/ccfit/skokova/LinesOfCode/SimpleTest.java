package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTest {
  String configName = "test/ru/nsu/ccfit/skokova/LinesOfCode/TestDirs/Testing/config.txt";
  String dirName = "test/ru/nsu/ccfit/skokova/LinesOfCode/TestDirs/Testing";

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

    Controller controller = new Controller(dirName, configName);
	  controller.fillStatisctics();
    Assert.assertEquals(0, controller.getStatistics().getStat().get(".cpp").getFilesCount());
    Assert.assertEquals(3, controller.getStatistics().getStat().get(".c").getFilesCount());
    Assert.assertEquals(15, controller.getStatistics().getStat().get(".c").getLinesCount());
    Assert.assertEquals(5, controller.getStatistics().getStat().get("Total").getFilesCount());
    Assert.assertEquals(33, controller.getStatistics().getStat().get("Total").getLinesCount());
    Assert.assertEquals(0, controller.getStatistics().getStat().get(".java").getFilesCount());
    Assert.assertEquals(5, controller.getStatistics().getStat().get(">1400000000").getFilesCount());
    Assert.assertEquals(33, controller.getStatistics().getStat().get(">1400000000").getLinesCount());
    System.out.println("TEST SimpleTest PASSED");
  }
}
