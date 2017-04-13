package ru.nsu.ccfit.skokova.LinesOfCode;

public class NotFilter implements Filter {
    Filter filter;

    public NotFilter(Filter f) {
        this.filter = f;
    }

    public boolean check(String fileName) {
        if (filter.check(fileName) == false) {
            return true;
        } else {
            return false;
        }
    }
}
