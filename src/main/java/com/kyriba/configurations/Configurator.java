package com.kyriba.configurations;

import com.kyriba.core.RecordStatistics;
import com.kyriba.core.RecordWriter;
import com.kyriba.filters.RecordFilterWorker;

public interface Configurator {
    void configure();

    int getThreadsCount();

    String getDirectoryPath();

    RecordWriter getRecordWriter();

    RecordFilterWorker getRecordFilterWorker();

    RecordStatistics getRecordStatistics();
}
