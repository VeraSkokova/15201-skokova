package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class AndFilter implements Filter {
    private ArrayList<Filter> filters;

    public AndFilter(ArrayList<Filter> f) {
        filters = f;
    }

    public boolean check(String fileName) {
        for (Filter f : this.filters) {
            if (f.check(fileName) == false) {
                return false;
            }
        }
        return true;
    }
}
