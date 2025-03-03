package com.catsriding.store.infra.time;

import com.catsriding.store.domain.shared.ClockHolder;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
