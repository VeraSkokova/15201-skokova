package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class AndFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put("&", AndFilterSerializer.class);
    }

    public AndFilterSerializer() {}

    private ArrayList<Filter> makeFilters(ArrayList<String> filterStrings) {
        ArrayList<Filter> result = new ArrayList<>();
        FilterSerializer temp;
        try {
            for (String s : filterStrings) {
                temp = FilterFactory.getInstance().createFilterSerializer(s.substring(0, 1));
                Filter tmp = temp.parseFilter(s);
                result.add(tmp);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Oops"); //TODO
        }
        return result;
    }

    public AndFilter parseFilter(String line) {
        ArrayList<String> filterStrings = new ArrayList<>();
        String cutLine = line.substring(2, line.length() - 1);
        int bracesCount = 0;
        int begin = 0;
        int end = 0;
        for (int i = 0; i < cutLine.length(); i++) {
            if (cutLine.charAt(i) == '(') {
                bracesCount++;
            }
            if (cutLine.charAt(i) == ')') {
                bracesCount--;
            }
            if ((cutLine.charAt(i) == ' ') && (bracesCount == 0)) {
                end = i;
                filterStrings.add(cutLine.substring(begin, end - 1));
                begin = end + 2;
            }
        }
        return new AndFilter(this.makeFilters(filterStrings));
    }
}
