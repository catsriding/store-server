package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.user.UserId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductUpdatableSpecTest {

    private final ProductUpdatableSpec spec = new ProductUpdatableSpec();

    @Test
    @DisplayName("✅ 수정 가능한 상품은 검증 통과")
    void shouldPassValidationForUpdatableProduct() {
        // Given
        Product updatableProduct = createProduct(ProductStatusType.SALE, false);

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updatableProduct));
    }

    @Test
    @DisplayName("🚫 삭제된 상품 상태인 경우 예외 발생")
    void shouldThrowExceptionWhenProductStatusIsDeleted() {
        // Given
        Product deletedStatusProduct = createProduct(ProductStatusType.DELETED, false);

        // When & Then
        assertThatThrownBy(() -> spec.check(deletedStatusProduct))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("삭제된 상품은 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("🚫 상품이 삭제 플래그인 경우 예외 발생")
    void shouldThrowExceptionWhenProductIsMarkedAsDeleted() {
        // Given
        Product markedAsDeletedProduct = createProduct(ProductStatusType.SALE, true);

        // When & Then
        assertThatThrownBy(() -> spec.check(markedAsDeletedProduct))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("이 상품은 삭제 상태로 수정할 수 없습니다.");
    }

    private Product createProduct(ProductStatusType status, boolean isDeleted) {
        return Product.builder()
                .id(ProductId.withId(1L))
                .sellerId(UserId.withId(1L))
                .name("테스트 상품")
                .description("테스트 상품 설명")
                .price(10000)
                .deliveryFee(3000)
                .statusType(status)
                .isDeleted(isDeleted)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}