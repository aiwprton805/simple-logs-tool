package com.kyriba.core;

import com.kyriba.data.Record;
import com.kyriba.groupers.GrouperState;
import com.kyriba.groupers.RecordGrouper;
import com.kyriba.groupers.TimeUnitGrouperState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RecordStatistics {
    private static final String PRINT_FORMAT = "%-20s\t|\t%15d%n";

    private final boolean isTimeUnitGrouping;
    private final ChronoUnit timeUnit;

    private final RecordGrouper recordGroup;
    private final Map<Object, Long> generalStats;

    public RecordStatistics(final GrouperState<String, Record> grouperState) {
        this.isTimeUnitGrouping = false;
        this.timeUnit = null;

        this.recordGroup = new RecordGrouper(grouperState);
        this.generalStats = new HashMap<>();
    }

    public RecordStatistics(GrouperState<LocalDateTime, Record> grouperState, ChronoUnit timeUnit) {
        isTimeUnitGrouping = true;
        this.timeUnit = timeUnit;

        this.recordGroup = new RecordGrouper(grouperState);
        this.generalStats = new HashMap<>();
    }

    public void accumulate(final Stream<Record> stream) {
        final Map<?, Long> partialStats = recordGroup.group(stream);
        partialStats.forEach((key, count) -> generalStats.merge(key, count, Long::sum));
    }

    public void print() {
        if (isTimeUnitGrouping) {
            printTimeUnitGroupedStatistics();
        } else {
            printUsernameGroupedStatistics();
        }
    }

    private void printUsernameGroupedStatistics() {
        System.out.println("      Username      \t|\t     Count     ");
        generalStats.forEach((key, count) -> System.out.printf(PRINT_FORMAT, key, count));
    }

    private void printTimeUnitGroupedStatistics() {
        if (TimeUnitGrouperState.isNotSupported(timeUnit)) {
            throw new IllegalArgumentException("timeUnit must been ChronoUnit.HOURS or ChronoUnit.DAYS or ChronoUnit.MONTHS.");
        }
        DateTimeFormatter fmt;
        switch (timeUnit) {
            case HOURS: {
                fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH");
                break;
            }
            case DAYS: {
                fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                break;
            }
            case MONTHS: {
                fmt = DateTimeFormatter.ofPattern("yyyy-MMM");
                break;
            }
            default:
                throw new IllegalStateException("Bad timeUnit: " + timeUnit);
        }
        System.out.println("        Time        \t|\t     Count     ");
        generalStats.forEach((key, count) -> System.out.printf(PRINT_FORMAT, ((LocalDateTime) key).format(fmt), count));
    }
}
