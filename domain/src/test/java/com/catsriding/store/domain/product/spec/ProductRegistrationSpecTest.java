package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.model.NewProduct;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductRegistrationSpecTest {

    private final ProductRegistrationSpec spec = new ProductRegistrationSpec();

    @Test
    @DisplayName("✅ 유효한 상품 데이터인 경우 검증 통과")
    void shouldPassValidationForValidProduct() {
        // Given
        NewProduct validProduct = new NewProduct(
                UserId.withId(1L),
                "커피 원두",
                "고품질 원두",
                15000,
                2500,
                ProductStatusType.SALE,
                false
        );

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(validProduct));
    }

    @Test
    @DisplayName("🚫 상품명이 누락된 경우 예외 발생")
    void shouldThrowExceptionWhenNameIsMissing() {
        // Given
        NewProduct invalidProduct = new NewProduct(
                UserId.withId(1L),
                "", // 상품명 없음
                "고품질 원두",
                15000,
                2500,
                ProductStatusType.SALE,
                false
        );

        // When & Then
        assertThatThrownBy(() -> spec.check(invalidProduct))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("상품명은 필수 입력 항목입니다.");
    }

    @Test
    @DisplayName("🚫 가격이 10원 미만인 경우 예외 발생")
    void shouldThrowExceptionWhenPriceIsTooLow() {
        // Given
        NewProduct invalidProduct = new NewProduct(
                UserId.withId(1L),
                "커피 원두",
                "고품질 원두",
                5, // 가격이 10원 미만
                2500,
                ProductStatusType.SALE,
                false
        );

        // When & Then
        assertThatThrownBy(() -> spec.check(invalidProduct))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("상품 가격은 최소 10원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("🚫 배송비가 0원 미만인 경우 예외 발생")
    void shouldThrowExceptionWhenDeliveryFeeIsNegative() {
        // Given
        NewProduct invalidProduct = new NewProduct(
                UserId.withId(1L),
                "커피 원두",
                "고품질 원두",
                15000,
                -500, // 배송비 음수
                ProductStatusType.SALE,
                false
        );

        // When & Then
        assertThatThrownBy(() -> spec.check(invalidProduct))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("배송비는 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("🚫 상태값이 유효하지 않은 경우 예외 발생")
    void shouldThrowExceptionWhenInvalidStatus() {
        // Given
        NewProduct invalidProduct = new NewProduct(
                UserId.withId(1L),
                "커피 원두",
                "고품질 원두",
                15000,
                2500,
                null,
                false
        );

        // When & Then
        assertThatThrownBy(() -> spec.check(invalidProduct))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("상품 상태가 유효하지 않습니다.");
    }

}