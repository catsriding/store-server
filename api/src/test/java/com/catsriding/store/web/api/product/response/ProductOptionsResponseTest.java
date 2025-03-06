package com.catsriding.store.web.api.product.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductOptionsResponseTest {

    private final LocalDateTime now = LocalDateTime.now();

    @Test
    @DisplayName("✅ SELECT 옵션 2개와 INPUT 옵션이 정상적으로 변환됨")
    void shouldConvertValidProductOptionsToResponse() {
        // When
        List<ProductOptionWithValue> input = List.of(
                // SELECT 1: 색상
                new ProductOptionWithValue(1L, 10L, "색상", ProductOptionType.SELECT, true, now, now,
                        100L, "레드", 1000, true, now, now),
                new ProductOptionWithValue(1L, 10L, "색상", ProductOptionType.SELECT, true, now, now,
                        101L, "블루", 1200, true, now, now),

                // SELECT 2: 사이즈
                new ProductOptionWithValue(2L, 10L, "사이즈", ProductOptionType.SELECT, true, now, now,
                        200L, "S", 0, true, now, now),
                new ProductOptionWithValue(2L, 10L, "사이즈", ProductOptionType.SELECT, true, now, now,
                        201L, "M", 500, true, now, now),
                new ProductOptionWithValue(2L, 10L, "사이즈", ProductOptionType.SELECT, true, now, now,
                        202L, "L", 1000, true, now, now),

                // INPUT: 각인 메시지
                new ProductOptionWithValue(3L, 10L, "각인 메시지", ProductOptionType.INPUT, true, now, now,
                        null, null, null, false, null, null)
        );
        ProductOptionsResponse response = ProductOptionsResponse.from(input);

        // Then
        assertThat(response.options()).isNotEmpty();
        assertThat(response.totalCount()).isEqualTo(3); // SELECT 두 개 + INPUT 하나

        // 🔹 SELECT 타입 1: 색상 옵션 검증
        ProductOptionsResponse.ProductOptionResponse colorOption = response.options().getFirst();
        ProductOptionWithValue firstColorOption = input.getFirst();

        assertThat(colorOption.optionId()).isEqualTo(String.valueOf(firstColorOption.optionId()));
        assertThat(colorOption.productId()).isEqualTo(String.valueOf(firstColorOption.productId()));
        assertThat(colorOption.name()).isEqualTo(firstColorOption.name());
        assertThat(colorOption.optionType()).isEqualTo(firstColorOption.optionType().name());
        assertThat(colorOption.usable()).isEqualTo(firstColorOption.usable());
        assertThat(colorOption.createdAt()).isEqualTo(firstColorOption.createdAt());
        assertThat(colorOption.updatedAt()).isEqualTo(firstColorOption.updatedAt());

        // 🔹 색상 옵션 값 검증 (레드, 블루)
        assertThat(colorOption.optionValues()).hasSize(2);

        ProductOptionsResponse.ProductOptionValueResponse redOption = colorOption.optionValues().get(0);
        ProductOptionsResponse.ProductOptionValueResponse blueOption = colorOption.optionValues().get(1);

        assertThat(redOption.optionValueId()).isEqualTo("100");
        assertThat(redOption.name()).isEqualTo("레드");
        assertThat(redOption.price()).isEqualTo(1000);
        assertThat(redOption.usable()).isEqualTo(true);
        assertThat(redOption.createdAt()).isEqualTo(firstColorOption.optionValueCreatedAt());
        assertThat(redOption.updatedAt()).isEqualTo(firstColorOption.optionValueUpdatedAt());

        assertThat(blueOption.optionValueId()).isEqualTo("101");
        assertThat(blueOption.name()).isEqualTo("블루");
        assertThat(blueOption.price()).isEqualTo(1200);
        assertThat(blueOption.usable()).isEqualTo(true);
        assertThat(blueOption.createdAt()).isEqualTo(firstColorOption.optionValueCreatedAt());
        assertThat(blueOption.updatedAt()).isEqualTo(firstColorOption.optionValueUpdatedAt());

        // 🔹 SELECT 타입 2: 사이즈 옵션 검증
        ProductOptionsResponse.ProductOptionResponse sizeOption = response.options().get(1);
        ProductOptionWithValue firstSizeOption = input.get(2);

        assertThat(sizeOption.optionId()).isEqualTo(String.valueOf(firstSizeOption.optionId()));
        assertThat(sizeOption.productId()).isEqualTo(String.valueOf(firstSizeOption.productId()));
        assertThat(sizeOption.name()).isEqualTo(firstSizeOption.name());
        assertThat(sizeOption.optionType()).isEqualTo(firstSizeOption.optionType().name());
        assertThat(sizeOption.usable()).isEqualTo(firstSizeOption.usable());
        assertThat(sizeOption.createdAt()).isEqualTo(firstSizeOption.createdAt());
        assertThat(sizeOption.updatedAt()).isEqualTo(firstSizeOption.updatedAt());

        // 🔹 사이즈 옵션 값 검증 (S, M, L)
        assertThat(sizeOption.optionValues()).hasSize(3);

        ProductOptionsResponse.ProductOptionValueResponse smallSize = sizeOption.optionValues().get(0);
        ProductOptionsResponse.ProductOptionValueResponse mediumSize = sizeOption.optionValues().get(1);
        ProductOptionsResponse.ProductOptionValueResponse largeSize = sizeOption.optionValues().get(2);

        assertThat(smallSize.optionValueId()).isEqualTo("200");
        assertThat(smallSize.name()).isEqualTo("S");
        assertThat(smallSize.price()).isEqualTo(0);
        assertThat(smallSize.usable()).isEqualTo(true);
        assertThat(smallSize.createdAt()).isEqualTo(firstSizeOption.optionValueCreatedAt());
        assertThat(smallSize.updatedAt()).isEqualTo(firstSizeOption.optionValueUpdatedAt());

        assertThat(mediumSize.optionValueId()).isEqualTo("201");
        assertThat(mediumSize.name()).isEqualTo("M");
        assertThat(mediumSize.price()).isEqualTo(500);
        assertThat(mediumSize.usable()).isEqualTo(true);
        assertThat(mediumSize.createdAt()).isEqualTo(firstSizeOption.optionValueCreatedAt());
        assertThat(mediumSize.updatedAt()).isEqualTo(firstSizeOption.optionValueUpdatedAt());

        assertThat(largeSize.optionValueId()).isEqualTo("202");
        assertThat(largeSize.name()).isEqualTo("L");
        assertThat(largeSize.price()).isEqualTo(1000);
        assertThat(largeSize.usable()).isEqualTo(true);
        assertThat(largeSize.createdAt()).isEqualTo(firstSizeOption.optionValueCreatedAt());
        assertThat(largeSize.updatedAt()).isEqualTo(firstSizeOption.optionValueUpdatedAt());

        // 🔹 INPUT 타입 (각인 메시지) 검증
        ProductOptionsResponse.ProductOptionResponse inputOption = response.options().get(2);
        ProductOptionWithValue firstInputOption = input.get(5);

        assertThat(inputOption.optionId()).isEqualTo(String.valueOf(firstInputOption.optionId()));
        assertThat(inputOption.productId()).isEqualTo(String.valueOf(firstInputOption.productId()));
        assertThat(inputOption.name()).isEqualTo(firstInputOption.name());
        assertThat(inputOption.optionType()).isEqualTo(firstInputOption.optionType().name());
        assertThat(inputOption.usable()).isEqualTo(firstInputOption.usable());
        assertThat(inputOption.createdAt()).isEqualTo(firstInputOption.createdAt());
        assertThat(inputOption.updatedAt()).isEqualTo(firstInputOption.updatedAt());

        // 🔹 INPUT 타입의 옵션 값은 없음
        assertThat(inputOption.optionValues()).isEmpty();
    }

    @Test
    @DisplayName("✅ INPUT 옵션만 있을 경우 정상적으로 변환됨")
    void shouldConvertProductOptionsWithOnlyInput() {
        // Given
        List<ProductOptionWithValue> input = List.of(
                new ProductOptionWithValue(4L, 11L, "각인 메시지", ProductOptionType.INPUT, true, now, now,
                        null, null, null, false, null, null)
        );

        // When
        ProductOptionsResponse response = ProductOptionsResponse.from(input);

        // Then
        assertThat(response.options()).hasSize(1);
        assertThat(response.totalCount()).isEqualTo(1);

        ProductOptionsResponse.ProductOptionResponse optionResponse = response.options().getFirst();
        assertThat(optionResponse.optionId()).isEqualTo("4");
        assertThat(optionResponse.productId()).isEqualTo("11");
        assertThat(optionResponse.name()).isEqualTo("각인 메시지");
        assertThat(optionResponse.optionType()).isEqualTo(ProductOptionType.INPUT.name());
        assertThat(optionResponse.usable()).isTrue();
        assertThat(optionResponse.optionValues()).isEmpty();
    }

    @Test
    @DisplayName("✅ INPUT 옵션과 SELECT 옵션이 함께 있을 경우 정상적으로 변환됨")
    void shouldConvertProductOptionsWithInputAndSelect() {
        // Given
        List<ProductOptionWithValue> input = List.of(
                // SELECT: 색상
                new ProductOptionWithValue(5L, 12L, "색상", ProductOptionType.SELECT, true, now, now,
                        300L, "그린", 900, true, now, now),
                new ProductOptionWithValue(5L, 12L, "색상", ProductOptionType.SELECT, true, now, now,
                        301L, "옐로우", 950, true, now, now),

                // INPUT: 각인 메시지
                new ProductOptionWithValue(6L, 12L, "각인 메시지", ProductOptionType.INPUT, true, now, now,
                        null, null, null, false, null, null)
        );

        // When
        ProductOptionsResponse response = ProductOptionsResponse.from(input);

        // Then
        assertThat(response.options()).hasSize(2);
        assertThat(response.totalCount()).isEqualTo(2);

        // SELECT 검증
        ProductOptionsResponse.ProductOptionResponse selectOption = response.options().get(0);
        assertThat(selectOption.optionId()).isEqualTo("5");
        assertThat(selectOption.productId()).isEqualTo("12");
        assertThat(selectOption.name()).isEqualTo("색상");
        assertThat(selectOption.optionType()).isEqualTo(ProductOptionType.SELECT.name());
        assertThat(selectOption.optionValues()).hasSize(2);
        assertThat(selectOption.optionValues().get(0).name()).isEqualTo("그린");
        assertThat(selectOption.optionValues().get(1).name()).isEqualTo("옐로우");

        // INPUT 검증
        ProductOptionsResponse.ProductOptionResponse inputOption = response.options().get(1);
        assertThat(inputOption.optionId()).isEqualTo("6");
        assertThat(inputOption.productId()).isEqualTo("12");
        assertThat(inputOption.name()).isEqualTo("각인 메시지");
        assertThat(inputOption.optionType()).isEqualTo(ProductOptionType.INPUT.name());
        assertThat(inputOption.optionValues()).isEmpty();
    }

    @Test
    @DisplayName("✅ 옵션이 없는 경우 빈 응답으로 변환됨")
    void shouldSuccessfullyConvertEmptyProductOptions() {
        // Given
        List<ProductOptionWithValue> input = List.of();

        // When
        ProductOptionsResponse response = ProductOptionsResponse.from(input);

        // Then
        assertThat(response.options()).isEmpty();
        assertThat(response.totalCount()).isEqualTo(0);
    }
}