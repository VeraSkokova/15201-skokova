package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;

public class LineCounter {
    private Statistics statistics;

    public LineCounter() {
        this.statistics = new Statistics();
    }

    public void count(String dirPath, ArrayList<FilterHolder> filters) throws IOException, DirOpeningException {
        File file = new File(dirPath);
        File[] files = file.listFiles();
        if (files == null) {
            throw new DirOpeningException("Can't open a a directory: " + file.getName());
        }
        for (File f : files) {
            int linesCount = 0;
            if (f.isDirectory() == true) {
                count(f.getAbsolutePath(), filters);
                continue;
            } else {
                boolean firstOpened = true;
                for (FilterHolder filter : filters) {
                    if (filter.getFilter().check(f.getAbsolutePath())) {
                        if (firstOpened) {
                            filter.getStatCounter().incFilesCount();
                            linesCount = countLinesInFile(f);
                            filter.getStatCounter().incLinesCount(linesCount);
                            this.statistics.incTotalLinesCount(linesCount);
                            this.statistics.incTotalFilesCount();
                            firstOpened = false;
                        } else {
                            filter.getStatCounter().incFilesCount();
                            filter.getStatCounter().incLinesCount(linesCount);
                        }
                    }
                }
            }
        }
        for (FilterHolder filterHolder : filters) {
            this.statistics.putInStatistics(filterHolder.getFilter().toString(), filterHolder.getStatCounter());
        }
    }

    private int countLinesInFile(File file) throws IOException {
        int count = 0;
        int readChars = 0;
        boolean empty = true;
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[1024];
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
        }
        return (count == 0 && !empty) ? 1 : count;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}

class FilterHolder {
    private Filter filter;
    private StatCounter statCounter;

    FilterHolder(Filter f) {
        this.filter = f;
        this.statCounter = new StatCounter();
    }

    public Filter getFilter() {
        return filter;
    }

    public StatCounter getStatCounter() {
        return statCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterHolder that = (FilterHolder) o;

        return filter != null ? filter.equals(that.filter) : that.filter == null;
    }

    @Override
    public int hashCode() {
        return filter != null ? filter.hashCode() : 0;
    }
}
