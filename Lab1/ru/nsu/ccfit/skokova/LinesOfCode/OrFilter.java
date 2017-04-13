package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class OrFilter implements Filter {
    private ArrayList<Filter> filters;

    public OrFilter(ArrayList<Filter> f) {
        this.filters = f;
    }

    public boolean check(String fileName) {
        for (Filter f : this.filters) {
            if (f.check(fileName) == true) {
                return true;
            }
        }
        return false;
    }
}
