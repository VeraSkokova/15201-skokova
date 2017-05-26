package ru.nsu.ccfit.skokova.LinesOfCode;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class Controller {
    private Statistics statistics;
    private String dirName;
    private String configFileName;

    public Controller() {
        statistics = new Statistics();
        dirName = "/";
    }

    public Controller(String directory, String configFile) {
        statistics = new Statistics();
        dirName = directory;
        configFileName = configFile;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void fillStatisctics() throws IOException {
        ArrayList<FilterHolder> filters = prepareFilters(this.configFileName);

        try {
            LineCounter lineCounter = new LineCounter();
            lineCounter.count(this.dirName, filters);
            this.statistics = lineCounter.getStatistics();
            //countLines(this.dirName, filters);
        } catch (DirOpeningException e) {
            System.out.println(e.getMessage());
        }

    }

    private boolean isEmptyAgregateFilter(Filter filter) {
        String filterName = filter.getClass().getSimpleName();
        if (filterName.equals("AndFilter")) {
            AndFilter andFilter = (AndFilter) filter;
            if (andFilter.getFilters().size() == 0) {
                return true;
            }
        }
        if (filterName.equals("OrFilter")) {
            OrFilter orFilter = (OrFilter) filter;
            if (orFilter.getFilters().size() == 0) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<FilterHolder> prepareFilters(String configFileName) {
        ArrayList<FilterHolder> filters = new ArrayList<>();
        try {
            ConfigParser configParser = new ConfigParser(configFileName);
            ArrayList<String> filterStrings = configParser.getFilterStrings();
            FilterSerializer filterSerializer;
            Filter filter;
            for (String fs : filterStrings) {
                try {
                    filterSerializer = FilterFactory.getInstance().createFilterSerializer(fs);
                    filter = filterSerializer.parseFilter(fs.trim());
                    if (this.isEmptyAgregateFilter(filter) == false) {
                        FilterHolder filterHolder = new FilterHolder(filter);
                        if (!filters.contains(filterHolder)) {
                            filters.add(filterHolder);
                        }
                    }
                } catch (IncorrectFilterTypeException e) {
                    System.err.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Can't create filter " + e.getMessage());
                } catch (FilterCreateException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Can't read configuration file " + e.getMessage());
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("Can't create this filter " + e.getMessage());
        }
        return filters;
    }
}

class DirOpeningException extends Exception {
    DirOpeningException(String message) {
        super(message);
    }
}

class FilterCreateException extends Exception {
    FilterCreateException(String message) {
        super(message);
    }
}
