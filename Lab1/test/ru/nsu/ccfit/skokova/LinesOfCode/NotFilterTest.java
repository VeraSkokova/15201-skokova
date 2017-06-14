package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotFilterTest {
  String configName = "test/ru/nsu/ccfit/skokova/LinesOfCode/TestDirs/Testing2/config.txt";
  String dirName = "test/ru/nsu/ccfit/skokova/LinesOfCode/TestDirs/Testing2";

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
    Assert.assertEquals(2, controller.getStatistics().getStat().get("!(.c)").getFilesCount());
    Assert.assertEquals(3, controller.getStatistics().getStat().get("!(.c)").getLinesCount());
    Assert.assertEquals(8, controller.getStatistics().getStat().get("!(&(.h >1400000000))").getLinesCount());
    Assert.assertEquals(2, controller.getStatistics().getStat().get("!(&(.h >1400000000))").getFilesCount());
    Assert.assertEquals(3, controller.getStatistics().getTotalFilesCount());
    Assert.assertEquals(9, controller.getStatistics().getTotalLinesCount());
    System.out.println("TEST NotFilterTest PASSED");
  }
}
