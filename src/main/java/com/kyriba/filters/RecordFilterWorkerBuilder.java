package com.kyriba.filters;

import com.kyriba.data.Record;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class RecordFilterWorkerBuilder {
    private final RecordFilterWorker recordFilterWorker;

    public RecordFilterWorkerBuilder() {
        this.recordFilterWorker = new RecordFilterWorker();
    }

    public RecordFilterWorkerBuilder username(final String username) {
        recordFilterWorker.setUsername(username);
        return this;
    }

    public RecordFilterWorkerBuilder period(final String startTime, final String endTime) {
        recordFilterWorker.setStartTime(LocalDateTime.parse(startTime, Record.DATE_TIME_FORMATTER));
        recordFilterWorker.setEndTime(LocalDateTime.parse(endTime, Record.DATE_TIME_FORMATTER));
        return this;
    }

    public RecordFilterWorkerBuilder messagePattern(final String regex) {
        recordFilterWorker.setMessagePattern(Pattern.compile(regex));
        return this;
    }

    public RecordFilterWorker build() {
        return recordFilterWorker;
    }
}
