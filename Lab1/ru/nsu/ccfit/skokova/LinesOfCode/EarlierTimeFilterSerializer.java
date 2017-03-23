package ru.nsu.ccfit.skokova.LinesOfCode;

public class EarlierTimeFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put("<", EarlierTimeFilterSerializer.class);
    }

    public EarlierTimeFilter parseFilter (String line) {

        return new EarlierTimeFilter(Long.parseLong(line.substring(1), 10));
    }
}
