package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.spec.option.InputTypeMustNotHaveValuesSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class InputTypeMustNotHaveValuesSpecTest {

    @Test
    @DisplayName("✅ INPUT 타입 옵션에 값이 없으면 검증 통과")
    void shouldPassWhenInputTypeHasNoValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "포장 방식", ProductOptionType.INPUT, true, List.of()
        );

        InputTypeMustNotHaveValuesSpec spec = new InputTypeMustNotHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 INPUT 타입 옵션에 값이 포함되면 예외 발생")
    void shouldThrowExceptionWhenInputTypeContainsValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "포장 방식", ProductOptionType.INPUT, true,
                List.of(new UpdateProductOption.UpdateProductOptionValue("선물 포장", 2000, true))
        );

        InputTypeMustNotHaveValuesSpec spec = new InputTypeMustNotHaveValuesSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("INPUT 타입 옵션에는 옵션 값을 포함할 수 없습니다.");
    }
}