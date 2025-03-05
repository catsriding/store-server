package com.catsriding.store.domain.product;

import static com.catsriding.store.domain.product.ProductOptionType.INPUT;
import static com.catsriding.store.domain.product.ProductOptionType.SELECT;

import com.catsriding.store.domain.product.model.NewProductOption;
import com.catsriding.store.domain.product.model.NewProductOption.NewProductOptionValue;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.product.spec.option.InputToSelectMustHaveValuesSpec;
import com.catsriding.store.domain.product.spec.option.InputTypeMustNotHaveValuesSpec;
import com.catsriding.store.domain.product.spec.option.OptionNameLengthSpec;
import com.catsriding.store.domain.product.spec.option.OptionPriceMustBeNonNegativeSpec;
import com.catsriding.store.domain.product.spec.option.ProductOwnershipSpec;
import com.catsriding.store.domain.product.spec.option.SelectToInputMustDeleteValuesSpec;
import com.catsriding.store.domain.product.spec.option.SelectTypeMustHaveValuesSpec;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.user.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

public class ProductOption {

    private final ProductOptionId id;
    private final ProductId productId;
    private final UserId sellerId;
    private final String name;
    private final ProductOptionType optionType;
    private final boolean usable;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<ProductOptionValue> optionValues;

    @Builder
    public ProductOption(
            ProductOptionId id,
            ProductId productId,
            UserId sellerId,
            String name,
            ProductOptionType optionType,
            boolean usable,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<ProductOptionValue> optionValues
    ) {
        this.id = id;
        this.productId = productId;
        this.sellerId = sellerId;
        this.name = name;
        this.optionType = optionType;
        this.usable = usable;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.optionValues = optionValues;
    }

    public Long id() {
        return id.id();
    }

    public ProductOptionId optionId() {
        return id;
    }

    public ProductId productId() {
        return productId;
    }

    public UserId sellerId() {
        return sellerId;
    }

    public String name() {
        return name;
    }

    public ProductOptionType optionType() {
        return optionType;
    }

    public boolean usable() {
        return usable;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public List<ProductOptionValue> optionValues() {
        return Collections.unmodifiableList(optionValues);
    }

    public static ProductOption from(NewProductOption newOption, ClockHolder clock) {
        ProductOptionId optionId = ProductOptionId.withoutId();
        List<ProductOptionValue> optionValues = toNewOptionValues(
                newOption.optionType(),
                newOption.optionValues(),
                optionId, clock
        );
        return ProductOption.builder()
                .id(optionId)
                .productId(ProductId.withId(newOption.productId()))
                .sellerId(UserId.withId(newOption.sellerId()))
                .name(newOption.name())
                .optionType(newOption.optionType())
                .usable(newOption.usable())
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .optionValues(optionValues)
                .build();
    }

    private static List<ProductOptionValue> toNewOptionValues(
            ProductOptionType optionType,
            List<NewProductOptionValue> newOptionValues,
            ProductOptionId optionId,
            ClockHolder clock
    ) {
        return switch (optionType) {
            case INPUT -> List.of();
            case SELECT -> newOptionValues.stream()
                    .map(optionValue -> ProductOptionValue.from(optionValue, optionId, clock))
                    .toList();
        };
    }

    public ProductOption updateProductOption(UpdateProductOption updateOption, ClockHolder clock) {
        new ProductOwnershipSpec(this).check(updateOption);
        new InputTypeMustNotHaveValuesSpec().check(updateOption);
        new SelectTypeMustHaveValuesSpec().check(updateOption);
        new InputToSelectMustHaveValuesSpec().check(updateOption);
        new SelectToInputMustDeleteValuesSpec(this).check(updateOption);
        new OptionPriceMustBeNonNegativeSpec().check(updateOption);
        new OptionNameLengthSpec().check(updateOption);

        boolean isOptionTypeChanged = !optionType.equals(updateOption.optionType());
        List<ProductOptionValue> optionValues = toUpdateOptionValues(
                updateOption.updateOptionValues(),
                isOptionTypeChanged,
                clock);

        return ProductOption.builder()
                .id(id)
                .productId(productId)
                .sellerId(sellerId)
                .name(updateOption.name())
                .optionType(updateOption.optionType())
                .usable(updateOption.usable())
                .isDeleted(isDeleted)
                .createdAt(createdAt)
                .updatedAt(clock.now())
                .optionValues(optionValues)
                .build();
    }

    private List<ProductOptionValue> toUpdateOptionValues(
            List<UpdateProductOptionValue> updateOptionValues,
            boolean isOptionTypeChanged,
            ClockHolder clock
    ) {
        if (isSameInputType(isOptionTypeChanged)) return List.of();
        if (isChangingFromSelectToInput(isOptionTypeChanged)) return markAllOptionValuesDeleted(clock);
        if (isChangingFromInputToSelect(isOptionTypeChanged)) return createNewOptionValues(updateOptionValues, clock);

        return mergeOptionValues(updateOptionValues, clock);
    }

    private boolean isSameInputType(boolean isOptionTypeChanged) {
        return optionType.equals(INPUT) && !isOptionTypeChanged;
    }

    private boolean isChangingFromSelectToInput(boolean isOptionTypeChanged) {
        return optionType.equals(SELECT) && isOptionTypeChanged;
    }

    private boolean isChangingFromInputToSelect(boolean isOptionTypeChanged) {
        return optionType.equals(INPUT) && isOptionTypeChanged;
    }

    private List<ProductOptionValue> markAllOptionValuesDeleted(ClockHolder clock) {
        return optionValues.stream()
                .map(optionValue -> optionValue.delete(clock))
                .toList();
    }

    private List<ProductOptionValue> createNewOptionValues(
            List<UpdateProductOptionValue> updateOptionValues,
            ClockHolder clock
    ) {
        return updateOptionValues.stream()
                .map(value -> ProductOptionValue.from(value, id, clock))
                .toList();
    }

    private List<ProductOptionValue> mergeOptionValues(
            List<UpdateProductOptionValue> updateOptionValues,
            ClockHolder clock
    ) {
        List<ProductOptionValue> nextOptionValues = new ArrayList<>();
        int prevSize = optionValues.size();
        int nextSize = updateOptionValues.size();

        // 기존 옵션 값 덮어쓰기
        for (int i = 0; i < Math.min(prevSize, nextSize); i++) {
            ProductOptionValue updatedValue = optionValues.get(i).update(updateOptionValues.get(i), clock);
            nextOptionValues.add(updatedValue);
        }

        // 새로운 옵션 값 추가
        if (nextSize > prevSize) {
            List<ProductOptionValue> additionalValues = updateOptionValues.subList(prevSize, nextSize).stream()
                    .map(value -> ProductOptionValue.from(value, id, clock))
                    .toList();
            nextOptionValues.addAll(additionalValues);
        }

        // 기존 값이 더 많았다면 남은 값 삭제 마킹
        if (prevSize > nextSize) {
            for (int i = nextSize; i < prevSize; i++) {
                nextOptionValues.add(optionValues.get(i).delete(clock));
            }
        }

        return nextOptionValues;
    }

    public ProductOption deleteProductOption(ClockHolder clock) {
        return ProductOption.builder()
                .id(id)
                .productId(productId)
                .sellerId(sellerId)
                .name(name)
                .optionType(optionType)
                .usable(usable)
                .isDeleted(true)
                .createdAt(createdAt)
                .updatedAt(clock.now())
                .build();
    }

    public ProductOptionIdentifier toIdentifier() {
        return new ProductOptionIdentifier(id, productId, sellerId);
    }

    public List<Long> optionValueIds() {
        return optionValues.stream().map(ProductOptionValue::id).toList();
    }
}
