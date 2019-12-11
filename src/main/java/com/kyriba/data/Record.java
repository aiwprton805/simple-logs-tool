package com.kyriba.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Record {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private final LocalDateTime time;
    private final String username;
    private final String message;

    public Record(final LocalDateTime time, final String username, final String message) {
        this.time = time;
        this.username = username;
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(time, record.time) &&
                Objects.equals(username, record.username) &&
                Objects.equals(message, record.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, username, message);
    }

    @Override
    public String toString() {
        return time.format(DATE_TIME_FORMATTER) + " " + username + ": " + message;
    }
}
