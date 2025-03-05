package com.catsriding.store.application.product.model;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductOptionUpdateTest {

    @Test
    @DisplayName("✅ ProductOptionUpdate → ProductOptionIdentifier 변환")
    void shouldConvertToProductOptionIdentifier() {
        // Given
        Long optionId = 1L;
        Long productId = 2L;
        Long sellerId = 3L;
        ProductOptionUpdate command = new ProductOptionUpdate(
                optionId,
                productId,
                sellerId,
                "색상",
                "SELECT",
                true,
                List.of(new ProductOptionUpdate.OptionValue("레드", 1000, true))
        );

        // When
        ProductOptionIdentifier identifier = command.toIdentifier();

        // Then
        assertThat(identifier.optionId().id()).isEqualTo(optionId);
        assertThat(identifier.productId().id()).isEqualTo(productId);
        assertThat(identifier.sellerId().id()).isEqualTo(sellerId);
    }

    @Test
    @DisplayName("✅ ProductOptionUpdate → UpdateProductOption 변환")
    void shouldConvertToUpdateProductOption() {
        // Given
        Long optionId = 1L;
        Long productId = 2L;
        Long sellerId = 3L;
        String name = "색상 선택";
        String optionType = "SELECT";
        boolean usable = true;
        List<ProductOptionUpdate.OptionValue> optionValues = List.of(
                new ProductOptionUpdate.OptionValue("레드", 1000, true),
                new ProductOptionUpdate.OptionValue("블루", 2000, true)
        );

        ProductOptionUpdate command = new ProductOptionUpdate(optionId,
                productId,
                sellerId,
                name,
                optionType,
                usable,
                optionValues);

        // When
        UpdateProductOption updateProductOption = command.toUpdateOption();

        // Then
        assertThat(updateProductOption.name()).isEqualTo(name);
        assertThat(updateProductOption.optionType()).isEqualTo(ProductOptionType.SELECT);
        assertThat(updateProductOption.usable()).isEqualTo(usable);
        assertThat(updateProductOption.updateOptionValues()).hasSize(2);
        assertThat(updateProductOption.updateOptionValues()).extracting(UpdateProductOptionValue::name)
                .containsExactly("레드", "블루");
    }

    @Test
    @DisplayName("✅ ProductOptionUpdate 옵션 값 → UpdateProductOptionValue 변환")
    void shouldConvertToUpdateProductOptionValues() {
        // Given
        List<ProductOptionUpdate.OptionValue> optionValues = List.of(
                new ProductOptionUpdate.OptionValue("레드", 1000, true),
                new ProductOptionUpdate.OptionValue("블루", 2000, false)
        );

        ProductOptionUpdate command = new ProductOptionUpdate(
                1L, 2L, 3L, "색상", "SELECT", true, optionValues
        );

        // When
        List<UpdateProductOptionValue> result = command.toUpdateOption().updateOptionValues();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().name()).isEqualTo("레드");
        assertThat(result.getFirst().price()).isEqualTo(1000);
        assertThat(result.getFirst().usable()).isTrue();

        assertThat(result.getLast().name()).isEqualTo("블루");
        assertThat(result.getLast().price()).isEqualTo(2000);
        assertThat(result.getLast().usable()).isFalse();
    }

    @Test
    @DisplayName("✅ ProductOptionUpdate 옵션 타입 문자열 → ProductOptionType Enum 변환")
    void shouldConvertOptionTypeStringToEnum() {
        // Given
        ProductOptionUpdate command = new ProductOptionUpdate(
                1L, 2L, 3L, "색상 선택", "INPUT", true, List.of()
        );

        // When
        UpdateProductOption updateProductOption = command.toUpdateOption();

        // Then
        assertThat(updateProductOption.optionType()).isEqualTo(ProductOptionType.INPUT);
    }

}