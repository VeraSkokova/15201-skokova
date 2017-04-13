package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public class OrFilterSerializer implements FilterSerializer {
    static {
        FilterFactory.creators.put("|", OrFilterSerializer.class);
    }

    public OrFilterSerializer() {}

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

    public OrFilter parseFilter(String line) {
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
        return new OrFilter(this.makeFilters(filterStrings));
    }
}
