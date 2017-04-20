package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class AndFilter implements Filter {
    private ArrayList<Filter> filters;
    private String filterString;

    public AndFilter(ArrayList<Filter> f, String s) {
        filters = f;
        filterString = s;
    }

    public ArrayList<Filter> getFilters() {
        return filters;
    }

    public boolean check(String fileName) {
        for (Filter f : this.filters) {
            if (f.check(fileName) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.filterString;
    }
}
