package ru.nsu.ccfit.skokova.LinesOfCode;

public interface FilterSerializer {
    public Filter parseFilter(String line) throws FilterCreateException;
}
