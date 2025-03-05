package com.catsriding.store.domain.product;

import static com.catsriding.store.domain.product.ProductOptionType.INPUT;
import static com.catsriding.store.domain.product.ProductOptionType.SELECT;
import static org.assertj.core.api.Assertions.assertThat;

import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.shared.fake.FakeClockHolder;
import com.catsriding.store.domain.user.UserId;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductOptionDomainUpdateTest {

    private ProductOption productOption;
    private ClockHolder clock;

    @BeforeEach
    void setUp() {
        clock = new FakeClockHolder(LocalDateTime.of(2025, 3, 5, 12, 0, 0));

        productOption = ProductOption.builder()
                .id(ProductOptionId.withId(1L))
                .productId(ProductId.withId(100L))
                .sellerId(UserId.withId(10L))
                .name("색상")
                .optionType(SELECT)
                .usable(true)
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .optionValues(List.of(
                        ProductOptionValue.builder()
                                .id(ProductOptionValueId.withId(101L))
                                .optionId(ProductOptionId.withId(1L))
                                .name("레드")
                                .price(1000)
                                .usable(true)
                                .isDeleted(false)
                                .createdAt(clock.now())
                                .updatedAt(clock.now())
                                .build(),
                        ProductOptionValue.builder()
                                .id(ProductOptionValueId.withId(102L))
                                .optionId(ProductOptionId.withId(1L))
                                .name("블루")
                                .price(1500)
                                .usable(true)
                                .isDeleted(false)
                                .createdAt(clock.now())
                                .updatedAt(clock.now())
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경 (같은 개수) 값 덮어쓰기")
    void shouldUpdateExistingValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1200, true),
                        new UpdateProductOptionValue("블루", 800, false)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::price)
                .containsExactly(1200, 800);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::usable)
                .containsExactly(true, false);
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경 (더 많은 개수) 기존 값 덮어쓰기 + 새 값 추가")
    void shouldAddNewValuesIfMoreThanExisting() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1200, true),
                        new UpdateProductOptionValue("블루", 800, true),
                        new UpdateProductOptionValue("그린", 1500, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(3);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::name)
                .containsExactly("레드", "블루", "그린");
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경 (더 적은 개수) 기존 값 덮어쓰기 + 남은 값 삭제 마킹")
    void shouldRemoveRemainingValuesIfFewerThanExisting() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1200, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues().get(0).isDeleted()).isFalse();
        assertThat(updatedOption.optionValues().get(1).isDeleted()).isTrue();
    }

    @Test
    @DisplayName("✅ SELECT → INPUT 변경 시 모든 옵션 값 삭제 마킹")
    void shouldMarkAllOptionValuesAsDeleted() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "포장 방식",
                INPUT, true, List.of()
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionType()).isEqualTo(INPUT);
        assertThat(updatedOption.optionValues()).allMatch(ProductOptionValue::isDeleted);
    }

    @Test
    @DisplayName("✅ INPUT → SELECT 변경 시 옵션 값 추가")
    void shouldAddNewOptionValuesWhenChangingInputToSelect() {
        // Given
        productOption = ProductOption.builder()
                .id(ProductOptionId.withId(2L))
                .productId(ProductId.withId(100L))
                .sellerId(UserId.withId(200L))
                .name("포장 방식")
                .optionType(INPUT)
                .usable(true)
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .optionValues(List.of())
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                2L, 100L, 200L, "포장 방식",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("기본 포장", 0, true),
                        new UpdateProductOptionValue("선물 포장", 2000, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::name)
                .containsExactly("기본 포장", "선물 포장");
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경 시 일부 옵션 값 비활성화")
    void shouldUpdateOptionValuesWithUsableStatus() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1000, false), // 비활성화
                        new UpdateProductOptionValue("블루", 500, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::usable)
                .containsExactly(false, true);
    }

    @Test
    @DisplayName("✅ SELECT 옵션 값 순서 변경 적용")
    void shouldRearrangeOptionValuesWhenOrderChanges() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("블루", 500, true), // 순서 변경
                        new UpdateProductOptionValue("레드", 1000, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::name)
                .containsExactly("블루", "레드");
    }

    @Test
    @DisplayName("✅ INPUT 옵션 이름 변경 적용")
    void shouldUpdateInputOptionName() {
        // Given
        productOption = ProductOption.builder()
                .id(ProductOptionId.withId(2L))
                .productId(ProductId.withId(100L))
                .sellerId(UserId.withId(200L))
                .name("기존 입력 옵션")
                .optionType(INPUT)
                .usable(true)
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .optionValues(List.of())
                .build();

        UpdateProductOption updateCommand = new UpdateProductOption(
                2L, 100L, 200L, "새로운 입력 옵션", // 옵션 이름 변경
                INPUT, true, List.of()
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.name()).isEqualTo("새로운 입력 옵션");
    }

    @Test
    @DisplayName("✅ SELECT 옵션 가격 수정 적용")
    void shouldUpdateOptionValuesPriceCorrectly() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1200, true),
                        new UpdateProductOptionValue("블루", 700, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::price)
                .containsExactly(1200, 700);
    }

    @Test
    @DisplayName("✅ 옵션 활성/비활성 상태 변경")
    void shouldUpdateOptionUsableStatus() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, false, // 옵션 비활성화
                List.of(
                        new UpdateProductOptionValue("레드", 1000, true),
                        new UpdateProductOptionValue("블루", 500, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.usable()).isFalse();
    }

    @Test
    @DisplayName("✅ 옵션 이름만 변경되고 나머지는 유지")
    void shouldUpdateOnlyOptionName() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "새로운 색상", // 옵션 이름 변경
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1000, true),
                        new UpdateProductOptionValue("블루", 1500, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.name()).isEqualTo("새로운 색상");
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::name)
                .containsExactly("레드", "블루");
    }

    @Test
    @DisplayName("✅ 기존 옵션 값 개수 동일하지만 일부 값 삭제 마킹")
    void shouldMarkSomeOptionValuesAsDeleted() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1000, true) // 블루는 삭제됨
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues().get(0).isDeleted()).isFalse();
        assertThat(updatedOption.optionValues().get(1).isDeleted()).isTrue();
    }

    @Test
    @DisplayName("✅ 기존 옵션 값 개수 동일하지만 일부 값의 활성 상태 변경")
    void shouldUpdateOptionUsableStatusForSomeValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1000, false), // 비활성화
                        new UpdateProductOptionValue("블루", 1500, true)
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(2);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::usable)
                .containsExactly(false, true);
    }

    @Test
    @DisplayName("✅ 기존 옵션 값 가격 변경 + 새로운 값 추가")
    void shouldUpdateOptionValuePricesAndAddNewValues() {
        // Given
        UpdateProductOption updateCommand = new UpdateProductOption(
                1L, 100L, 10L, "색상",
                SELECT, true,
                List.of(
                        new UpdateProductOptionValue("레드", 1200, true), // 가격 변경
                        new UpdateProductOptionValue("블루", 900, true), // 가격 변경
                        new UpdateProductOptionValue("그린", 1800, true) // 새 값 추가
                )
        );

        // When
        ProductOption updatedOption = productOption.updateProductOption(updateCommand, clock);

        // Then
        assertThat(updatedOption.optionValues()).hasSize(3);
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::name)
                .containsExactly("레드", "블루", "그린");
        assertThat(updatedOption.optionValues()).extracting(ProductOptionValue::price)
                .containsExactly(1200, 900, 1800);
    }
}