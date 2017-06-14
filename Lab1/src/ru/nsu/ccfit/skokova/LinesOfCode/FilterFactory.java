package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.HashMap;
import java.util.Map;

public class FilterFactory {
    static Map<String, Class> creators;

    static {
        creators = new HashMap<>();
    }

    private static FilterFactory instance;

    private FilterFactory() {}

    public static FilterFactory getInstance() {
        if (instance == null) {
            instance = new FilterFactory();
        }
        return instance;
    }

    public static Map<String, Class> getCreators() {
        return creators;
    }

    public FilterSerializer createFilterSerializer(String s) throws InstantiationException, IllegalAccessException, IncorrectFilterTypeException { //TODO: проверка на null
        String sTrimmed = s.trim();
        if (!(creators.containsKey(sTrimmed.substring(0, 1)))) {
            throw new IncorrectFilterTypeException("Incorrect type of filter: " + sTrimmed);
        }
        FilterSerializer result = (FilterSerializer) creators.get(sTrimmed.substring(0, 1)).newInstance();
        return result;
    }
}

class IncorrectFilterTypeException extends Exception {
    IncorrectFilterTypeException(String message) {
        super(message);
    }
}