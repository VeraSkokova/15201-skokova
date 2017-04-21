package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTest {
  String configName = "./Testing/config.txt";
  String dirName = "./Testing/";

  @Test
  public void test() throws IOException {
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
