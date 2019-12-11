package com.kyriba.groupers;

import com.kyriba.data.Record;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class UsernameGrouperState implements GrouperState<String, Record> {
    @Override
    public Map<String, Long> group(final Stream<Record> stream) {
        return stream.collect(groupingBy(Record::getUsername, counting()));
    }
}
