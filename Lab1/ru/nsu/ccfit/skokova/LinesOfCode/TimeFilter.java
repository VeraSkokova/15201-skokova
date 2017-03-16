package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.File;

public class TimeFilter implements Filter {
    private long time;
    private char timeDirection;

    TimeFilter(long time, char timeDirection) {
        this.time = time;
        this.timeDirection = timeDirection;
    }

    public boolean check(String fileName) {
        File file = new File(fileName);
        long modTime = file.lastModified();
        if (timeDirection == '<') {
            return modTime < time;
        }
        else {
            return modTime > time;
        }
    }
}
