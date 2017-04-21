package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class OrFilterSerializer extends AgregateFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put("|", OrFilterSerializer.class);
    }

    public OrFilterSerializer() {}

    public OrFilter parseFilter(String line) {
        ArrayList<String> filterStrings = super.parseConfig(line);
        return new OrFilter(super.makeFilters(filterStrings), line);
    }
}
