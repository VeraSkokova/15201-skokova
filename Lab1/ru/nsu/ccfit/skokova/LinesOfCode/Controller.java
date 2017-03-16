package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.Scanner;

public class Controller {
    private Statistics statistics;
    private String dirName;

    public Controller() {
        statistics = new Statistics();
        dirName = "/";
    }

    public Controller(String directory) {
        statistics = new Statistics();
        dirName = directory;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void fillStatisctics() throws IOException {
        StatCounter temp = new StatCounter();

        countLines(this.dirName, temp);

        this.statistics.putInStatistics("Total", temp);
    }

    private void countLines(String dirPath, StatCounter counter) throws IOException{
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory() == true) {
                countLines(f.getAbsolutePath(), counter);
                continue;
            }
            else  {
                counter.incFilesCount();
                FileReader helper = new FileReader(f);
                BufferedReader reader = new BufferedReader(helper);
                //FileReader reader = new FileReader(f);
                //Scanner reader = new Scanner(f);
                while (reader.readLine() != null) {
                    counter.incLinesCount();
                }
            }
        }
    }
}







