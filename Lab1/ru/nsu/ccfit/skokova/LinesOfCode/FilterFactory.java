package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.HashMap;
import java.util.Map;

public class FilterFactory {
    static Map<String, Class> creators = new HashMap<>();

    private static FilterFactory instance;

    private FilterFactory() {}

    public static FilterFactory getInstance() {
        if (instance == null) {
            instance = new FilterFactory();
        }
        return instance;
    }


    public FilterSerializer createFilterSerializer(String s) throws InstantiationException, IllegalAccessException { //TODO: проверка на null
        String sTrimmed = s.trim();
        return  (FilterSerializer) creators.get(sTrimmed.substring(0, 1)).newInstance();
    }
}
