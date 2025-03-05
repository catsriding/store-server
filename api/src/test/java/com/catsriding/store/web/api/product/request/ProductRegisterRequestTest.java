package com.catsriding.store.web.api.product.request;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.web.shared.InvalidRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductRegisterRequestTest {

    @Test
    @DisplayName("✅ 유효한 상품 데이터인 경우 검증 통과")
    void shouldPassValidationForValidProduct() {
        // Given
        String name = "상품명";
        String description = "상품 설명";
        int price = 100;
        int deliveryFee = 0;
        String statusType = "SALE";

        // When & Then
        assertThatNoException().isThrownBy(() -> new ProductRegisterRequest(name,
                description,
                price,
                deliveryFee,
                statusType));
    }

    @Test
    @DisplayName("🚫 상품명이 누락된 경우 예외 발생")
    void shouldThrowExceptionWhenNameIsMissing() {
        // Given
        String name = "";
        String description = "상품 설명";
        int price = 100;
        int deliveryFee = 0;
        String statusType = "SALE";

        // When & Then
        assertThatThrownBy(() -> new ProductRegisterRequest(name, description, price, deliveryFee, statusType))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("상품명을 입력해 주세요.");
    }

    @Test
    @DisplayName("🚫 상품 설명이 누락된 경우 예외 발생")
    void shouldThrowExceptionWhenDescriptionIsMissing() {
        // Given
        String name = "상품명";
        String description = "";
        int price = 100;
        int deliveryFee = 0;
        String statusType = "SALE";

        // When & Then
        assertThatThrownBy(() -> new ProductRegisterRequest(name, description, price, deliveryFee, statusType))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("상품 설명을 입력해 주세요.");
    }

    @Test
    @DisplayName("🚫 가격이 10원 미만인 경우 예외 발생")
    void shouldThrowExceptionWhenPriceIsTooLow() {
        // Given
        String name = "상품명";
        String description = "상품 설명";
        int price = 5; // 10원 미만
        int deliveryFee = 0;
        String statusType = "SALE";

        // When & Then
        assertThatThrownBy(() -> new ProductRegisterRequest(name, description, price, deliveryFee, statusType))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("상품 가격은 최소 10원 이상이어야 합니다. 다시 입력해 주세요.");
    }

    @Test
    @DisplayName("🚫 배송비가 0원 미만인 경우 예외 발생")
    void shouldThrowExceptionWhenDeliveryFeeIsNegative() {
        // Given
        String name = "상품명";
        String description = "상품 설명";
        int price = 100;
        int deliveryFee = -500; // 음수
        String statusType = "SALE";

        // When & Then
        assertThatThrownBy(() -> new ProductRegisterRequest(name, description, price, deliveryFee, statusType))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("배송비는 무료 또는 일정 금액 이상이어야 합니다.");
    }

    @Test
    @DisplayName("🚫 상태값이 유효하지 않은 경우 예외 발생")
    void shouldThrowExceptionWhenInvalidStatus() {
        // Given
        String name = "상품명";
        String description = "상품 설명";
        int price = 100;
        int deliveryFee = 0;
        String statusType = "INVALID"; // 잘못된 상태값

        // When & Then
        assertThatThrownBy(() -> new ProductRegisterRequest(name, description, price, deliveryFee, statusType))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("상품 상태가 잘못되었습니다. 다시 확인해 주세요.");
    }
}