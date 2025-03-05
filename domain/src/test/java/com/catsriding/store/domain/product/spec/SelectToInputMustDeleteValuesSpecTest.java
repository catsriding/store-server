package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionId;
import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.ProductOptionValue;
import com.catsriding.store.domain.product.ProductOptionValueId;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.product.spec.option.SelectToInputMustDeleteValuesSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class SelectToInputMustDeleteValuesSpecTest {

    @Test
    @DisplayName("✅ SELECT → INPUT 변경 시 기존 옵션 값이 존재하고 새로운 값이 없으면 검증 통과")
    void shouldPassWhenSelectToInputWithExistingValuesAndNoNewValues() {
        // Given
        ProductOption productOption = ProductOption.builder()
                .optionType(ProductOptionType.SELECT)
                .optionValues(List.of(
                        ProductOptionValue.builder()
                                .id(ProductOptionValueId.withoutId())
                                .optionId(ProductOptionId.withoutId())
                                .name("기본 배송")
                                .price(0)
                                .usable(true)
                                .isDeleted(false)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ))
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 방식", ProductOptionType.INPUT, true, List.of()
        );

        SelectToInputMustDeleteValuesSpec spec = new SelectToInputMustDeleteValuesSpec(productOption);

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 SELECT → INPUT 변경 시 기존 옵션 값이 없으면 예외 발생")
    void shouldThrowExceptionWhenSelectToInputWithoutExistingValues() {
        // Given
        ProductOption productOption = ProductOption.builder()
                .optionType(ProductOptionType.SELECT)
                .optionValues(List.of()) // 기존 옵션 값이 없음
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 방식", ProductOptionType.INPUT, true, List.of()
        );

        SelectToInputMustDeleteValuesSpec spec = new SelectToInputMustDeleteValuesSpec(productOption);

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("SELECT 타입에서 INPUT 타입으로 변경할 때, 기존 옵션 값이 존재해야 하며, 새로운 옵션 값은 포함될 수 없습니다.");
    }

    @Test
    @DisplayName("🚫 SELECT → INPUT 변경 시 업데이트 요청에 옵션 값이 포함되면 예외 발생")
    void shouldThrowExceptionWhenSelectToInputWithNewValues() {
        // Given
        ProductOption productOption = ProductOption.builder()
                .optionType(ProductOptionType.SELECT)
                .optionValues(List.of(
                        ProductOptionValue.builder()
                                .id(ProductOptionValueId.withoutId())
                                .optionId(ProductOptionId.withoutId())
                                .name("기본 배송")
                                .price(0)
                                .usable(true)
                                .isDeleted(false)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ))
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 2L, 3L, "배송 방식", ProductOptionType.INPUT, true,
                List.of(new UpdateProductOptionValue("새로운 옵션", 1000, true))
        );

        SelectToInputMustDeleteValuesSpec spec = new SelectToInputMustDeleteValuesSpec(productOption);

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("SELECT 타입에서 INPUT 타입으로 변경할 때, 기존 옵션 값이 존재해야 하며, 새로운 옵션 값은 포함될 수 없습니다.");
    }
}