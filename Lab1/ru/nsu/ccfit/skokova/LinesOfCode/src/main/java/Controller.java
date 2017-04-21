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

        filters.add(new FilterHolder(new TotalFilter()));

        try {
            countLines(this.dirName, filters);
        } catch (DirOpeningException e) {
            System.out.println(e.getMessage());
        }

    }

    private void countLines(String dirPath, ArrayList<FilterHolder> filters) throws IOException, DirOpeningException {
        File file = new File(dirPath);
        File[] files = file.listFiles();
        if (files == null) {
            throw new DirOpeningException("Can't open a a directory: " + file.getName());
        }
        for (File f : files) {
            if (f.isDirectory() == true) {
                countLines(f.getAbsolutePath(), filters);
                continue;
            }
            else  {
                for (FilterHolder filter : filters) {
                    if (filter.getFilter().check(f.getAbsolutePath())) {
                        filter.getStatCounter().incFilesCount();
                        FileReader helper = new FileReader(f);
                        BufferedReader reader = new BufferedReader(helper);
                        while (reader.readLine() != null) {
                            filter.getStatCounter().incLinesCount();
                        }
                        helper.close();
                        reader.close();
                    }
                }
            }
        }
        for (FilterHolder filterHolder : filters) {
            this.statistics.putInStatistics(filterHolder.getFilter().toString(), filterHolder.getStatCounter());
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
                        filters.add(filterHolder);
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

    public void printStatistics() {
        Set<String> keys = this.getStatistics().getStat().keySet();
        ArrayList<PrintHelper> printHelpers = new ArrayList<>();
        for (String s : keys) {
            printHelpers.add(new PrintHelper(s, this.getStatistics().getStat().get(s)));
        }
        if (printHelpers.isEmpty()) {
            System.out.println("Nothing to show");
            return;
        }
        printHelpers.sort(new PrintComparator());
        if (printHelpers.get(0).getStatCounter().getFilesCount() == 0) {
            System.out.println("Empty directory");
        } else {
            System.out.println(printHelpers.get(0).getFilterName() + " - " + printHelpers.get(0).getStatCounter().getLinesCount() + " lines in " + printHelpers.get(0).getStatCounter().getFilesCount() + " files");
            System.out.println("------------");
        }
        for (int i = 1; i < printHelpers.size(); i++) {
            if (printHelpers.get(i).getStatCounter().getFilesCount() == 0) {
                continue;
            }
            System.out.println(printHelpers.get(i).getFilterName() + " - " + printHelpers.get(i).getStatCounter().getLinesCount() + " lines in " + printHelpers.get(i).getStatCounter().getFilesCount() + " files");
        }
    }
}

class FilterHolder {
    private Filter filter;
    private StatCounter statCounter;

    FilterHolder(Filter f) {
        this.filter = f;
        this.statCounter = new StatCounter();
    }

    public Filter getFilter() {
        return filter;
    }

    public StatCounter getStatCounter() {
        return statCounter;
    }
}

class PrintHelper {
    private String filterName;
    private StatCounter statCounter;

    public String getFilterName() {
        return filterName;
    }

    public StatCounter getStatCounter() {
        return statCounter;
    }

    PrintHelper(String name, StatCounter counter) {
        this.filterName = name;
        this.statCounter = counter;
    }
}

class PrintComparator implements Comparator<PrintHelper> {
    @Override
    public int compare(PrintHelper first, PrintHelper second) {
        if (first.getFilterName().equals("Total")) {
            return -1;
        } else if (second.getFilterName().equals("Total")) {
            return 1;
        } else {
            return first.getStatCounter().getFilesCount() - second.getStatCounter().getFilesCount();
        }
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






