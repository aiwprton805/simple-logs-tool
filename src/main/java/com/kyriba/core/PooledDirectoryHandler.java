package com.kyriba.core;

import com.kyriba.data.Record;
import com.kyriba.filters.RecordFilterWorker;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PooledDirectoryHandler implements DirectoryHandler {
    private static final PathMatcher LOG_PATH_MATCHER = FileSystems.getDefault().getPathMatcher("glob:**.{log,txt}");

    private final Path dirPath;
    private final ExecutorService executorService;

    private final RecordWriter recordWriter;
    private final RecordFilterWorker recordFilterWorker;
    private final RecordStatistics recordStats;

    protected PooledDirectoryHandler(final Path dirPath, final RecordWriter recordWriter,
                                     final RecordFilterWorker recordFilterWorker, final RecordStatistics recordStats) {
        this(dirPath, 1, recordWriter, recordFilterWorker, recordStats);
    }

    protected PooledDirectoryHandler(final Path dirPath, final int threads, final RecordWriter recordWriter,
                                     final RecordFilterWorker recordFilterWorker, final RecordStatistics recordStats) {
        this.dirPath = dirPath;
        this.executorService = Executors.newFixedThreadPool(threads);
        this.recordWriter = recordWriter;
        this.recordFilterWorker = recordFilterWorker;
        this.recordStats = recordStats;
    }

    @Override
    public void handle() throws IOException, InterruptedException, ExecutionException {
        List<RecordExtractor> tasks = prepareFileTasks();
        List<Future<List<Record>>> futures = executeFileTasks(tasks);
        writeFilteredResults(futures);
        printStatistics(futures);
    }

    private List<RecordExtractor> prepareFileTasks() throws IOException {
        try (Stream<Path> paths = Files.list(dirPath)) {
            return paths
                    .filter(path -> Files.isRegularFile(path) && LOG_PATH_MATCHER.matches(path))
                    .map(RecordExtractor::new)
                    .collect(Collectors.toList());
        }
    }

    private List<Future<List<Record>>> executeFileTasks(final List<RecordExtractor> fileTasks) throws InterruptedException {
        List<Future<List<Record>>> futures = executorService.invokeAll(fileTasks);
        executorService.shutdown();
        return futures;
    }

    private void writeFilteredResults(final List<Future<List<Record>>> futures) throws IOException, InterruptedException, ExecutionException {
        for (Future<List<Record>> future : futures) {
            recordWriter.write(recordFilterWorker.filter(future.get().stream()));
        }
    }

    private void printStatistics(final List<Future<List<Record>>> futures) throws InterruptedException, ExecutionException {
        for (Future<List<Record>> future : futures) {
            recordStats.accumulate(future.get().parallelStream());
        }
        recordStats.print();
    }
}
