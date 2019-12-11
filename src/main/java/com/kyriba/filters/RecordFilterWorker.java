package com.kyriba.filters;

import com.kyriba.data.Record;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RecordFilterWorker implements FilterWorker<Record> {
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Pattern messagePattern;

    public RecordFilterWorker() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Pattern getMessagePattern() {
        return messagePattern;
    }

    public void setMessagePattern(Pattern messagePattern) {
        this.messagePattern = messagePattern;
    }

    @Override
    public Stream<Record> filter(Stream<Record> stream) {
        if (username != null)
            stream = stream.filter(record -> record.getUsername().equals(username));
        if (startTime != null && endTime != null)
            stream = stream.filter(record -> record.getTime().isAfter(startTime) && record.getTime().isBefore(endTime));
        if (messagePattern != null)
            stream = stream.filter(record -> messagePattern.matcher(record.getMessage()).matches());
        return stream;
    }
}
