package ru.nsu.ccfit.skokova.LinesOfCode;

public class NotFilter implements Filter {
    private Filter filter;
    private String filterString;

    public NotFilter(Filter f, String s) {
        this.filter = f;
        this.filterString = s;
    }

    public boolean check(String fileName) {
        if (filter.check(fileName) == false) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.filterString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotFilter notFilter = (NotFilter) o;

        return filter.equals(notFilter.filter);
    }

    @Override
    public int hashCode() {
        return filter.hashCode();
    }
}
