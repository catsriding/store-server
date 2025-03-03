package com.catsriding.store.domain.shared.sonyflake.core;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
@Slf4j
class SonyflakeTest {

    @Test
    @DisplayName("generate unique id")
    void shouldGenerateUniqueId() throws Exception {

        //  Given
        Sonyflake instance = Sonyflake.getInstance();

        //  When
        long id = instance.nextId();

        //  Then
        log.info("shouldGenerateUniqueId: generatedId={}", id);
        Assertions.assertThat(id).isGreaterThan(0);

    }

}