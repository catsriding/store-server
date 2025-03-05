package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.spec.option.OptionNameLengthSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class OptionNameLengthSpecTest {

    @DisplayName("✅ 옵션 이름과 값의 길이가 제한 내이면 검증 통과")
    @Test
    void shouldPassWhenOptionNameAndValuesWithinLengthLimit() {
        // Given
        String optionNameWithLength25 = RandomString.make(25); // 정확히 25자
        String optionValueWithLength30 = RandomString.make(30); // 정확히 30자

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, optionNameWithLength25, ProductOptionType.SELECT, true,
                List.of(
                        new UpdateProductOption.UpdateProductOptionValue(optionValueWithLength30, 1000, true),
                        new UpdateProductOption.UpdateProductOptionValue("정상 값", 500, false)
                )
        );

        OptionNameLengthSpec spec = new OptionNameLengthSpec();

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @DisplayName("🚫 옵션 이름이 25자를 초과하면 예외 발생")
    @Test
    void shouldThrowExceptionWhenOptionNameExceedsMaxLength() {
        // Given
        String optionNameWithLength26 = RandomString.make(26); // 초과된 26자

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, optionNameWithLength26, ProductOptionType.SELECT, true,
                List.of(new UpdateProductOption.UpdateProductOptionValue("정상 값", 1000, true))
        );

        OptionNameLengthSpec spec = new OptionNameLengthSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("옵션 이름은 최대 25자, 옵션 값 이름은 최대 30자까지 입력할 수 있습니다.");
    }

    @DisplayName("🚫 옵션 값 이름이 30자를 초과하면 예외 발생")
    @Test
    void shouldThrowExceptionWhenOptionValueNameExceedsMaxLength() {
        // Given
        String optionValueWithLength31 = RandomString.make(31); // 초과된 31자

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "정상 옵션 이름", ProductOptionType.SELECT, true,
                List.of(new UpdateProductOption.UpdateProductOptionValue(optionValueWithLength31, 1000, true))
        );

        OptionNameLengthSpec spec = new OptionNameLengthSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("옵션 이름은 최대 25자, 옵션 값 이름은 최대 30자까지 입력할 수 있습니다.");
    }

    @DisplayName("🚫 옵션 이름과 옵션 값 중 하나라도 길이를 초과하면 예외 발생")
    @Test
    void shouldThrowExceptionWhenEitherOptionNameOrValueExceedsMaxLength() {
        // Given
        String optionNameWithLength26 = RandomString.make(26); // 초과된 26자
        String optionValueWithLength31 = RandomString.make(31); // 초과된 31자

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, optionNameWithLength26, ProductOptionType.SELECT, true,
                List.of(new UpdateProductOption.UpdateProductOptionValue(optionValueWithLength31, 1000, true))
        );

        OptionNameLengthSpec spec = new OptionNameLengthSpec();

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("옵션 이름은 최대 25자, 옵션 값 이름은 최대 30자까지 입력할 수 있습니다.");
    }
}