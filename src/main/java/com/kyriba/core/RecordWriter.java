package com.kyriba.core;

import com.kyriba.data.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class RecordWriter {
    private final Path outFilePath;

    public RecordWriter(final Path outFilePath) {
        this.outFilePath = outFilePath;
    }

    public RecordWriter(final String outFilePath) {
        this.outFilePath = Paths.get(outFilePath);
    }

    public void write(final Stream<Record> stream) throws IOException {
        Files.write(outFilePath, stream.map(Record::toString).collect(Collectors.toList()), CREATE, APPEND, WRITE);
    }
}
