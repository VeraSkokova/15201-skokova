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

    public void setLinesCount(int linesCount) {
        this.linesCount = linesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
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

    public void incLinesCount(int n) {
        this.linesCount+=n;
    }
    public void incFilesCount(int n) {
        this.filesCount+=n;
    }

    public void setToZero() {
        this.linesCount = 0;
        this.filesCount = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatCounter counter = (StatCounter) o;

        if (linesCount != counter.linesCount) return false;
        return filesCount == counter.filesCount;
    }

    @Override
    public int hashCode() {
        int result = linesCount;
        result = 31 * result + filesCount;
        return result;
    }

}

