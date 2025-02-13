package com.catsriding.store.domain.shared.sonyflake.spec;

import com.catsriding.store.domain.shared.sonyflake.exception.InvalidStartTimeException;
import java.time.Instant;

public class SonyflakeStartTimeSpec {

    /**
     * Validates the provided SonyflakeSettings.
     *
     * @param settings the SonyflakeSettings to validate.
     */
    public void check(Instant startTime) {
        validateStartTime(startTime);
    }

    private void validateStartTime(Instant startTime) {
        if (startTime == null) {
            throw new InvalidStartTimeException("Start time cannot be null");
        }
        if (startTime.isAfter(Instant.now())) {
            throw new InvalidStartTimeException("Start time cannot be in the future: " + startTime);
        }
    }
}
