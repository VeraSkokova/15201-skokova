package ru.nsu.ccfit.skokova.LinesOfCode;

import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;
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

        countLines(this.dirName, filters);

    }

    private void countLines(String dirPath, ArrayList<FilterHolder> filters) throws IOException {
        File file = new File(dirPath);
        File[] files = file.listFiles();
        StatCounter counter = new StatCounter();
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
                    }
                }
            }
        }
        for (FilterHolder filterHolder : filters) {
            this.statistics.putInStatistics(filterHolder.getFilter().toString(), filterHolder.getStatCounter());
        }
    }

    private ArrayList<FilterHolder> prepareFilters(String configFileName) {
        ArrayList<FilterHolder> filters = new ArrayList<>();
        try {
            ConfigParser configParser = new ConfigParser(configFileName);
            ArrayList<String> filterStrings = configParser.getFilterStrings();
            FilterSerializer filterSerializer;
            Filter filter;
            for (String fs : filterStrings) {
                filterSerializer = FilterFactory.getInstance().createFilterSerializer(fs);
                filter = filterSerializer.parseFilter(fs.trim());
                FilterHolder filterHolder = new FilterHolder(filter);
                filters.add(filterHolder);
            }
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            System.out.println("Oops"); //TODO
        }
        return filters;
    }

    public void printStatistics() {
        Set<String> keys = this.getStatistics().getStat().keySet();
        for (String s : keys) {
            StatCounter temp = this.getStatistics().getStat().get(s);
            if (temp.getFilesCount() == 0) {
                continue;
            }
            System.out.println(s.toString() + " " + temp.getLinesCount() + " lines in " + temp.getFilesCount() + " files");
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






