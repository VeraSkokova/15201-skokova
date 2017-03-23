package ru.nsu.ccfit.skokova.LinesOfCode;


public class LaterTimeFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put(">", LaterTimeFilterSerializer.class);
    }

    public LaterTimeFilter parseFilter (String line) {

        return new LaterTimeFilter(Long.parseLong(line.substring(1), 10));
    }
}
