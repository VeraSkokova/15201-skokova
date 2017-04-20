package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class AndFilterSerializer extends AgregateFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put("&", AndFilterSerializer.class);
    }

    public AndFilterSerializer() {}

    public AndFilter parseFilter(String line) {
        ArrayList<String> filterStrings = super.parseConfig(line);
        return new AndFilter(super.makeFilters(filterStrings), line);
    }
}
