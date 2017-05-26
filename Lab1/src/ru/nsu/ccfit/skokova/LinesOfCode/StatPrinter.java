package ru.nsu.ccfit.skokova.LinesOfCode;

import java.util.ArrayList;
import java.util.Set;
import java.util.Comparator;

public class StatPrinter {
    private Statistics statistics;

    public StatPrinter() {
        this.statistics = new Statistics();
    }

    public StatPrinter(Statistics stats) {
        this.statistics = stats;
    }

    public void printStatistics() {
        Set<String> keys = this.statistics.getStat().keySet();
        ArrayList<PrintHelper> printHelpers = new ArrayList<>();
        System.out.println("Total" + " - " + this.statistics.getTotalLinesCount() + " lines in " + this.statistics.getTotalFilesCount() + " files");
        for (String s : keys) {
            printHelpers.add(new PrintHelper(s, this.statistics.getStat().get(s)));
        }
        if (printHelpers.isEmpty()) {
            System.out.println("Nothing to show");
            return;
        }
        printHelpers.sort(new PrintComparator());
        System.out.println("------------");
        for (int i = 0; i < printHelpers.size(); i++) {
            if (printHelpers.get(i).getStatCounter().getFilesCount() == 0) {
                continue;
            }
            System.out.println(printHelpers.get(i).getFilterName() + " - " + printHelpers.get(i).getStatCounter().getLinesCount() + " lines in " + printHelpers.get(i).getStatCounter().getFilesCount() + " files");
        }
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
        return second.getStatCounter().getFilesCount() - first.getStatCounter().getFilesCount();
    }
}
