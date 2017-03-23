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
}
