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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrFilter orFilter = (OrFilter) o;

        /*int size = filters.size() > orFilter.filters.size() ? filters.size() : orFilter.filters.size();
        for (int i = 0; i < size; i++) {
            if (!(filters.get(i).equals(orFilter.filters.get(i)))) {
                return false;
            }
        }
        return true;*/
        return filters.equals(orFilter.filters);
    }

    @Override
    public int hashCode() {
        return filters.hashCode();
    }
}
