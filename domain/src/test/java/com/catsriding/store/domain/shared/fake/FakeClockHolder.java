package com.catsriding.store.domain.shared.fake;

import com.catsriding.store.domain.shared.ClockHolder;
import java.time.LocalDateTime;

public class FakeClockHolder implements ClockHolder {

    private final LocalDateTime fixedTime;

    public FakeClockHolder(LocalDateTime fixedTime) {
        this.fixedTime = fixedTime;
    }

    @Override
    public LocalDateTime now() {
        return fixedTime;
    }

    @Override
    public long currentTimeMillis() {
        return 0;
    }
}
