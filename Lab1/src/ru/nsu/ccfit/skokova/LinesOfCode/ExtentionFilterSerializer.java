package ru.nsu.ccfit.skokova.LinesOfCode;

public class ExtentionFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put(".", ExtentionFilterSerializer.class);
    }
    public ExtentionFilterSerializer() {}

    public ExtentionFilter parseFilter(String line) throws FilterCreateException {
        if (Character.isWhitespace(line.charAt(1))) {
            throw new FilterCreateException("Excess whitespace in " + line);
        }
        return new ExtentionFilter(line);
    }
}
