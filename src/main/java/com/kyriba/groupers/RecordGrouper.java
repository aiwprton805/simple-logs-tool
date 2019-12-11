package com.kyriba.groupers;

import com.kyriba.data.Record;

import java.util.Map;
import java.util.stream.Stream;

public class RecordGrouper {
    private GrouperState<?, Record> recordGroupState;

    public RecordGrouper(final GrouperState<?, Record> recordGroupState) {
        this.recordGroupState = recordGroupState;
    }

    public void changeState(final GrouperState<?, Record> state) {
        this.recordGroupState = state;
    }

    public Map<?, Long> group(final Stream<Record> stream) {
        return recordGroupState.group(stream);
    }
}
