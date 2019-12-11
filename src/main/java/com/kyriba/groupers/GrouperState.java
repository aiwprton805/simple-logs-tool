package com.kyriba.groupers;

import java.util.Map;
import java.util.stream.Stream;

public interface GrouperState<R, T> {
    Map<R, Long> group(Stream<T> stream);
}
