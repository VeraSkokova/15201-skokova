package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private Map<String, StatCounter> stat;
    private int filtersCount;

    public Statistics() {
        this.stat = new HashMap<>();
        this.filtersCount = 0;
    }

    public Map<String, StatCounter> getStat() {
        return stat;
    }

    public void setStat(Map<String, StatCounter> stat) {
        this.stat = stat;
    }

    public void putInStatistics(String filterName, StatCounter counts) {
        this.stat.put(filterName, counts);
    }
}

class StatCounter {
    private int linesCount;
    private int filesCount;

    public StatCounter() {
        this.linesCount = 0;
        this.filesCount = 0;
    }

    public int getLinesCount() {
        return linesCount;
    }
    public int getFilesCount() {
        return filesCount;
    }

    public void incLinesCount() {
        this.linesCount++;
    }
    public void incFilesCount() {
        this.filesCount++;
    }
}

