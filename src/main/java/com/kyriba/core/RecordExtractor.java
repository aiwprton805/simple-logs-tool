package com.kyriba.core;

import com.kyriba.data.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordExtractor implements Callable<List<Record>> {
    private static final Pattern RECORD_PATTERN = Pattern.compile("(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}) (.*): (.*)");

    private final Path filePath;

    public RecordExtractor(Path filePath) {
        this.filePath = filePath;
    }

    public RecordExtractor(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    @Override
    public List<Record> call() throws IOException {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                    .map(RecordExtractor::stringToRecord)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    private static Record stringToRecord(final String line) {
        Matcher m = RECORD_PATTERN.matcher(line);
        if (m.matches())
            return new Record(LocalDateTime.parse(m.group(1), Record.DATE_TIME_FORMATTER), m.group(2), m.group(3));
        return null;
    }
}
