package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.File;

public class ExtentionFilter implements Filter {
    private String extension;

    ExtentionFilter(String extension) {
        this.extension = extension;
    }

    public boolean check(String fileName) {
        File file = new File(fileName);
        if (file.getName().endsWith(this.extension)) {
            return true;
        }
        else {
            return false;
        }
    }
}
