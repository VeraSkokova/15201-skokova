package ru.nsu.ccfit.skokova.LinesOfCode;

public class ExtentionFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put(".", ExtentionFilterSerializer.class);
    }

    public ExtentionFilter parseFilter(String line) {
        return new ExtentionFilter(line);
    }
}
