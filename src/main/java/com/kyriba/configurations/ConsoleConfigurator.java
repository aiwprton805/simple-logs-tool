package com.kyriba.configurations;

import com.kyriba.core.RecordStatistics;
import com.kyriba.core.RecordWriter;
import com.kyriba.filters.RecordFilterWorker;
import com.kyriba.filters.RecordFilterWorkerBuilder;
import com.kyriba.groupers.TimeUnitGrouperState;
import com.kyriba.groupers.UsernameGrouperState;

import java.time.temporal.ChronoUnit;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;

public class ConsoleConfigurator implements Configurator {

    private boolean usernameFilterOn;
    private boolean timePeriodFilterOn;
    private boolean messageFilterOn;

    private String usernameFilterValue;
    private String timePeriodFilterStartValue;
    private String timePeriodFilterEndValue;
    private String messageFilterValue;

    private boolean usernameGroupingOn;
    private boolean timeUnitGroupingOn;

    private String timeUnitGroupingValue;

    private int threadsCount;
    private String dirPath;
    private String outputFilePath;

    private RecordWriter recordWriter;
    private RecordFilterWorker recordFilterWorker;
    private RecordStatistics recordStats;

    public ConsoleConfigurator() {
    }

    @Override
    public void configure() {
        inputFilterParams();
        inputGroupingParams();
        inputOtherParams();

        initRecordFilter();
        initRecordFilterWorker();
        initRecordStatistics();
    }

    @Override
    public int getThreadsCount() {
        return threadsCount;
    }

    @Override
    public String getDirectoryPath() {
        return dirPath;
    }

    @Override
    public RecordWriter getRecordWriter() {
        return recordWriter;
    }

    @Override
    public RecordFilterWorker getRecordFilterWorker() {
        return recordFilterWorker;
    }

    @Override
    public RecordStatistics getRecordStatistics() {
        return recordStats;
    }

    private void initRecordFilter() {
        recordWriter = new RecordWriter(outputFilePath);
    }

    private void initRecordFilterWorker() {
        RecordFilterWorkerBuilder recordFilterWorkerBuilder = new RecordFilterWorkerBuilder();
        if (usernameFilterOn)
            recordFilterWorkerBuilder = recordFilterWorkerBuilder.username(usernameFilterValue);
        if (timePeriodFilterOn)
            recordFilterWorkerBuilder = recordFilterWorkerBuilder.period(timePeriodFilterStartValue, timePeriodFilterEndValue);
        if (messageFilterOn)
            recordFilterWorkerBuilder = recordFilterWorkerBuilder.messagePattern(messageFilterValue);
        recordFilterWorker = recordFilterWorkerBuilder.build();
    }

    private void initRecordStatistics() {
        if (usernameGroupingOn)
            recordStats = new RecordStatistics(new UsernameGrouperState());
        if (timeUnitGroupingOn) {
            ChronoUnit timeUnit = null;
            switch (timeUnitGroupingValue) {
                case "h":
                case "H": {
                    timeUnit = ChronoUnit.HOURS;
                    break;
                }
                case "d":
                case "D": {
                    timeUnit = ChronoUnit.DAYS;
                    break;
                }
                case "m":
                case "M": {
                    timeUnit = ChronoUnit.MONTHS;
                    break;
                }
            }
            recordStats = new RecordStatistics(new TimeUnitGrouperState(timeUnit), timeUnit);
        }
    }

    private void inputFilterParams() {
        out.println("Filter parameters:");

        usernameFilterOn = askUser("\tFilter by username? (y / n)");
        if (usernameFilterOn) usernameFilterValue = takeUserInput("\tEnter username");
        timePeriodFilterOn = askUser("\tFilter by time period? (y / n)");
        if (timePeriodFilterOn) {
            timePeriodFilterStartValue = takeUserInput("\tEnter start time (yyyy/MM/dd HH:mm:ss)",
                    "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
            timePeriodFilterEndValue = takeUserInput("\tEnter end time (yyyy/MM/dd HH:mm:ss)",
                    "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
        }
        messageFilterOn = askUser("\tFilter by message pattern? (y / n)");
        if (messageFilterOn) messageFilterValue = takeUserInput("\tEnter message pattern");
    }

    private void inputGroupingParams() {
        out.println("Grouping parameters:");
        out.println("\tYou can use grouping by time unit or by username.");
        usernameGroupingOn = askUser("\tUse grouping by username? (y / n)");
        timeUnitGroupingOn = !usernameGroupingOn;
        if (timeUnitGroupingOn) {
            out.println("\tYou use time unit grouping.");
            timeUnitGroupingValue = takeUserInput("\tChoose time unit: m - months, d - days, h - hours", "[mdh]");
        }
    }

    private void inputOtherParams() {
        out.println("Other parameters:");
        threadsCount = takeNumberUserInput("\tEnter count of threads");
        if (threadsCount < 1 || threadsCount > 1000) {
            out.println("\tOne thread will be used.");
            threadsCount = 1;
        }
        dirPath = takeUserInput("\tEnter Directory path");
        outputFilePath = takeUserInput("\tEnter output file path");
    }

    private boolean askUser(final String question) {
        boolean badInput = true;
        while (badInput) {
            badInput = false;
            Scanner scanner = new Scanner(System.in);
            out.println(question);
            try {
                String answer = scanner.next("[YNyn]");
                return answer.equalsIgnoreCase("Y");
            } catch (InputMismatchException e) {
                out.println("\tBad input, try again.");
                badInput = true;
            }
        }
        return false;
    }

    private String takeUserInput(final String invitation, final String regex) {
        boolean badInput = true;
        while (badInput) {
            badInput = false;
            Scanner scanner = new Scanner(System.in);
            out.println(invitation);
            try {
                String input = scanner.findInLine(regex);
                if (input == null) {
                    out.println("\tBad input, try again.");
                    badInput = true;
                    continue;
                }
                return input;
            } catch (InputMismatchException e) {
                out.println("\tBad input, try again.");
                badInput = true;
            }
        }
        return null;
    }

    private String takeUserInput(final String invitation) {
        Scanner scanner = new Scanner(System.in);
        out.println(invitation);
        return scanner.nextLine();
    }

    private int takeNumberUserInput(final String invitation) {
        boolean badInput = true;
        while (badInput) {
            badInput = false;
            Scanner scanner = new Scanner(System.in);
            out.println(invitation);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                out.println("\tBad input, try again.");
                badInput = true;
            }
        }
        return 1;
    }
}
