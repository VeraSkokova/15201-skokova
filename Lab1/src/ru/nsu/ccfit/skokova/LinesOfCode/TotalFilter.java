package ru.nsu.ccfit.skokova.LinesOfCode;

public class TotalFilter implements Filter {
    public TotalFilter() {}

    public boolean check(String fileName) {
        return true;
    }

    @Override
    public String toString() {
        return "Total";
    }
}
