package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class OrFilter implements Filter {
    private ArrayList<Filter> filters;
    private String filterString;

    public OrFilter(ArrayList<Filter> f, String s) {
        this.filters = f;
        this.filterString = s;
    }

    public ArrayList<Filter> getFilters() {
        return filters;
    }

    public boolean check(String fileName) {
        for (Filter f : this.filters) {
            if (f.check(fileName) == true) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.filterString;
    }
}
