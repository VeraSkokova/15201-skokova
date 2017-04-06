package ru.nsu.ccfit.skokova.LinesOfCode;

public class ExtentionFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.getInstance().creators.put(".", ExtentionFilterSerializer.class);
    }
    public ExtentionFilterSerializer() {}

    public ExtentionFilter parseFilter(String line) {
        return new ExtentionFilter(line);
    }
}
