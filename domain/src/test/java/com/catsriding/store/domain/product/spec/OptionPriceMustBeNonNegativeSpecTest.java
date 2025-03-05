package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.product.spec.option.OptionPriceMustBeNonNegativeSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class OptionPriceMustBeNonNegativeSpecTest {

    @Test
    @DisplayName("✅ 모든 옵션 값의 가격이 0 이상이면 검증 통과")
    void shouldPassWhenAllOptionValuesHaveNonNegativePrices() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "색상 옵션",
                ProductOptionType.SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1000, true),
                        new UpdateProductOptionValue("블루", 500, false),
                        new UpdateProductOptionValue("그린", 0, true) // 가격이 0일 수도 있음
                )
        );

        OptionPriceMustBeNonNegativeSpec spec = new OptionPriceMustBeNonNegativeSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 옵션 값 중 하나라도 가격이 0 미만이면 예외 발생")
    void shouldThrowExceptionWhenAtLeastOneOptionValueHasNegativePrice() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "색상 옵션",
                ProductOptionType.SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1000, true),
                        new UpdateProductOptionValue("블루", -500, true), // 🚨 가격이 음수
                        new UpdateProductOptionValue("그린", 300, false)
                )
        );

        OptionPriceMustBeNonNegativeSpec spec = new OptionPriceMustBeNonNegativeSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("옵션 값의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("🚫 옵션 값 중 여러 개가 음수 가격이면 예외 발생")
    void shouldThrowExceptionWhenMultipleOptionValuesHaveNegativePrices() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "색상 옵션",
                ProductOptionType.SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", -1000, true), // 🚨 가격이 음수
                        new UpdateProductOptionValue("블루", -500, true), // 🚨 가격이 음수
                        new UpdateProductOptionValue("그린", 300, true)
                )
        );

        OptionPriceMustBeNonNegativeSpec spec = new OptionPriceMustBeNonNegativeSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("옵션 값의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("✅ 옵션 타입이 INPUT 이면 가격이 없어도 검증 통과")
    void shouldPassWhenInputTypeWithoutPrices() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "사용자 입력",
                ProductOptionType.INPUT, true, List.of() // 옵션 값 없음
        );

        OptionPriceMustBeNonNegativeSpec spec = new OptionPriceMustBeNonNegativeSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("✅ 옵션 값이 없을 경우 검증 통과")
    void shouldPassWhenNoOptionValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 방식",
                ProductOptionType.SELECT, true, List.of() // 옵션 값 없음
        );

        OptionPriceMustBeNonNegativeSpec spec = new OptionPriceMustBeNonNegativeSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }
}