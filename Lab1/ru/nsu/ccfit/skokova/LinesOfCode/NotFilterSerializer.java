package ru.nsu.ccfit.skokova.LinesOfCode;

public class NotFilterSerializer implements FilterSerializer {
    public NotFilter parseFilter(String line) {
        String forParse = line.substring(2, line.length() - 1);
        NotFilter result = null;
        try {
            result = new NotFilter(FilterFactory.getInstance().createFilterSerializer(forParse).parseFilter(forParse));
        } catch (InstantiationException | IllegalAccessException e) {
        System.out.println("Oops"); //TODO
    }
        return result;
    }
}
