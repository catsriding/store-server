package com.catsriding.store.domain.product.spec;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.spec.option.ProductOwnershipSpec;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.user.UserId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductOwnershipSpecTest {

    @Test
    @DisplayName("✅ 옵션을 수정할 권한이 있는 경우 검증 통과")
    void shouldPassWhenUserHasOwnership() {
        // Given
        ProductOption productOption = ProductOption.builder()
                .productId(ProductId.withId(10L))
                .sellerId(UserId.withId(20L))
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 10L, 20L, "색상", ProductOptionType.SELECT, true, List.of()
        );

        ProductOwnershipSpec spec = new ProductOwnershipSpec(productOption);

        // When & Then
        assertThatNoException().isThrownBy(() -> spec.check(updateCommand));
    }

    @Test
    @DisplayName("🚫 옵션을 수정할 권한이 없으면 예외 발생")
    void shouldThrowExceptionWhenUserHasNoOwnership() {
        // Given
        ProductOption productOption = ProductOption.builder()
                .productId(ProductId.withId(10L))
                .sellerId(UserId.withId(20L))
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 11L, 21L, "색상", ProductOptionType.SELECT, true, List.of()
        );

        ProductOwnershipSpec spec = new ProductOwnershipSpec(productOption);

        // When & Then
        assertThatThrownBy(() -> spec.check(updateCommand))
                .isInstanceOf(GenericSpecificationException.class)
                .hasMessage("해당 옵션을 수정할 권한이 없습니다.");
    }
}