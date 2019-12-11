package com.kyriba.groupers;

import com.kyriba.data.Record;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.*;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class TimeUnitGrouperState implements GrouperState<LocalDateTime, Record> {
    private final ChronoUnit timeUnit;

    public static boolean isSupported(final ChronoUnit timeUnit) {
        return timeUnit == HOURS || timeUnit == DAYS || timeUnit == MONTHS;
    }

    public static boolean isNotSupported(final ChronoUnit timeUnit) {
        return !isSupported(timeUnit);
    }

    public TimeUnitGrouperState(final ChronoUnit timeUnit) {
        if (isNotSupported(timeUnit)) {
            throw new IllegalArgumentException("timeUnit must been ChronoUnit.HOURS or ChronoUnit.DAYS or ChronoUnit.MONTHS.");
        }
        this.timeUnit = timeUnit;
    }

    @Override
    public Map<LocalDateTime, Long> group(final Stream<Record> stream) {
        switch (timeUnit) {
            case HOURS:
                return stream.collect(groupingBy(record -> record.getTime().truncatedTo(HOURS), counting()));
            case DAYS:
                return stream.collect(groupingBy(record -> record.getTime().truncatedTo(DAYS), counting()));
            case MONTHS:
                return stream.collect(groupingBy(record -> record.getTime()
                        .with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(DAYS), counting()));
        }
        throw new IllegalStateException("Bad timeUnit: " + timeUnit);
    }
}
