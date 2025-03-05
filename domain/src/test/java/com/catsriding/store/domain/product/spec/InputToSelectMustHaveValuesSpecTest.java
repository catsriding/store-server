package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.product.spec.option.InputToSelectMustHaveValuesSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class InputToSelectMustHaveValuesSpecTest {

    @Test
    @DisplayName("✅ INPUT → SELECT 변경 시 옵션 값이 포함되면 검증 통과")
    void shouldPassWhenChangingToSelectWithValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 옵션", ProductOptionType.SELECT, true,
                List.of(new UpdateProductOptionValue("기본 배송", 0, true)) // 옵션 값 포함
        );

        InputToSelectMustHaveValuesSpec spec = new InputToSelectMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 INPUT → SELECT 변경 시 옵션 값이 없으면 예외 발생")
    void shouldThrowExceptionWhenChangingToSelectWithoutValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 옵션", ProductOptionType.SELECT, true, List.of() // 옵션 값 없음
        );

        InputToSelectMustHaveValuesSpec spec = new InputToSelectMustHaveValuesSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("SELECT 타입으로 변경하려면 최소 하나 이상의 옵션 값이 필요합니다.");
    }

    @Test
    @DisplayName("✅ INPUT → SELECT 변경 시 모든 옵션 값이 비활성화되어 있어도 검증 통과")
    void shouldPassWhenChangingToSelectWithInactiveValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 옵션", ProductOptionType.SELECT, true,
                List.of(new UpdateProductOptionValue("기본 배송", 0, false)) // 비활성화된 옵션 값 포함
        );

        InputToSelectMustHaveValuesSpec spec = new InputToSelectMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("✅ INPUT 타입 유지 시 옵션 값이 없어도 검증 통과")
    void shouldPassWhenKeepingInputTypeWithoutValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 옵션", ProductOptionType.INPUT, true, List.of()
        );

        InputToSelectMustHaveValuesSpec spec = new InputToSelectMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("✅ INPUT → SELECT 변경 시 최소 한 개 이상의 옵션 값이 포함되면 검증 통과")
    void shouldPassWhenChangingToSelectWithAtLeastOneValue() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 옵션", ProductOptionType.SELECT, true,
                List.of(
                        new UpdateProductOptionValue("기본 배송", 0, false), // 비활성화된 옵션
                        new UpdateProductOptionValue("빠른 배송", 2000, true) // 활성화된 옵션
                )
        );

        InputToSelectMustHaveValuesSpec spec = new InputToSelectMustHaveValuesSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }
}