package com.kyriba.filters;

import java.util.stream.Stream;

public interface FilterWorker<T> {
    Stream<T> filter(Stream<T> stream);
}
