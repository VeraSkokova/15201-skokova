package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;

public abstract class AgregateFilterSerializer implements FilterSerializer {
    ArrayList<Filter> makeFilters(ArrayList<String> filterStrings) {
        ArrayList<Filter> result = new ArrayList<>();
        FilterSerializer temp;
        try {
            for (String s : filterStrings) {
                temp = FilterFactory.getInstance().createFilterSerializer(s.trim());
                Filter tmp = temp.parseFilter(s);
                result.add(tmp);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("Can't create filter: " + e.getMessage());
        } catch (IncorrectFilterTypeException e) {
            System.err.println(e.getMessage() + "(in agregate filter)");
            result = new ArrayList<>();
            return result;
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Too much spaces in agregate filter ");
            result = new ArrayList<>();
            return result;
        } catch (FilterCreateException e) {
            System.err.println(e.getMessage());
            result = new ArrayList<>();
            return result;
        }
        return result;
    }

    public ArrayList<String> parseConfig(String line) {
        ArrayList<String> filterStrings = new ArrayList<>();
        String cutLine = line.trim().substring(2, line.length() - 1);
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
            if (((cutLine.charAt(i) == ' ') && (bracesCount == 0))) {
                end = i;
                filterStrings.add(cutLine.substring(begin, end));
                begin = end + 1;
            }
            if (i == cutLine.length() - 1) {
                filterStrings.add(cutLine.substring(begin));
            }
        }
        return filterStrings;
    }
}
