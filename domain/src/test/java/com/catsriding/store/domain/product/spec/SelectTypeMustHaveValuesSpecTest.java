package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.product.spec.option.SelectTypeMustHaveValuesSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class SelectTypeMustHaveValuesSpecTest {

    @Test
    @DisplayName("✅ SELECT 타입이며 최소 하나 이상의 옵션 값이 포함된 경우 검증 통과")
    void shouldPassWhenSelectTypeHasValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "색상 선택",
                ProductOptionType.SELECT, true,
                List.of(new UpdateProductOptionValue("레드", 1000, true))
        );

        SelectTypeMustHaveValuesSpec spec = new SelectTypeMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 SELECT 타입인데 옵션 값이 포함되지 않으면 예외 발생")
    void shouldThrowExceptionWhenSelectTypeHasNoValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "색상 선택",
                ProductOptionType.SELECT, true,
                List.of() // 옵션 값 없음
        );

        SelectTypeMustHaveValuesSpec spec = new SelectTypeMustHaveValuesSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("SELECT 타입 옵션에는 최소 하나 이상의 옵션 값이 필요합니다.");
    }

    @Test
    @DisplayName("✅ INPUT 타입이면 옵션 값이 없어도 검증 통과")
    void shouldPassWhenInputTypeHasNoValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 방법",
                ProductOptionType.INPUT, true,
                List.of()
        );

        SelectTypeMustHaveValuesSpec spec = new SelectTypeMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("✅ 기존 SELECT 타입에서 INPUT 타입으로 변경하는 경우 옵션 값 없어도 검증 통과")
    void shouldPassWhenChangingFromSelectToInputWithoutValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 방식",
                ProductOptionType.INPUT, true,
                List.of()
        );

        SelectTypeMustHaveValuesSpec spec = new SelectTypeMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 SELECT 타입이면서 옵션 값이 하나도 없는 경우 예외 발생")
    void shouldThrowExceptionWhenSelectTypeAndEmptyValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "사이즈 선택",
                ProductOptionType.SELECT, true,
                List.of() // 옵션 값 없음
        );

        SelectTypeMustHaveValuesSpec spec = new SelectTypeMustHaveValuesSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("SELECT 타입 옵션에는 최소 하나 이상의 옵션 값이 필요합니다.");
    }
}