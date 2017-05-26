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

    @Override
    public String toString() {
        return extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtentionFilter that = (ExtentionFilter) o;

        return extension != null ? extension.equals(that.extension) : that.extension == null;
    }

    @Override
    public int hashCode() {
        return extension != null ? extension.hashCode() : 0;
    }
}
