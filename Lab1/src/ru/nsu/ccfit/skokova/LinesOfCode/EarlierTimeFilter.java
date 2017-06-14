package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.File;

public class EarlierTimeFilter implements Filter {
    private long time;

    EarlierTimeFilter(long time) {
        this.time = time;
    }

    public boolean check(String fileName) {
        File file = new File(fileName);
        long modTime = file.lastModified();
        if (modTime < time) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "<" + time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EarlierTimeFilter that = (EarlierTimeFilter) o;

        return time == that.time;
    }

    @Override
    public int hashCode() {
        return (int) (time ^ (time >>> 32));
    }
}
