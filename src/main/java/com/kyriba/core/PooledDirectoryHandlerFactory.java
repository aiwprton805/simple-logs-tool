package com.kyriba.core;

import com.kyriba.configurations.Configurator;

import java.nio.file.Paths;

public class PooledDirectoryHandlerFactory implements DirectoryHandlerFactory {
    private final Configurator configurator;

    public PooledDirectoryHandlerFactory(final Configurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public DirectoryHandler newDirectoryHandler() {
        configurator.configure();
        return new PooledDirectoryHandler(Paths.get(configurator.getDirectoryPath()),
                configurator.getThreadsCount(),
                configurator.getRecordWriter(),
                configurator.getRecordFilterWorker(),
                configurator.getRecordStatistics());

    }
}
