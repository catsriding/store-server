package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OptionPriceMustBeNonNegativeSpec extends AbstractSpecification<ProductOptionContext> {

    @Override
    public boolean isSatisfiedBy(ProductOptionContext context) {
        return context.optionValues().stream()
                .allMatch(value -> value.price() >= 0);
    }

    @Override
    public void check(ProductOptionContext context) throws GenericSpecificationException {
        if (!isSatisfiedBy(context)) {
            log.warn(
                    "OptionPriceMustBePositiveSpec failed: Option value price must be >= 0. optionId={}, productId={}, sellerId={}, invalidPrices={}",
                    context.optionId(),
                    context.productId(),
                    context.sellerId(),
                    context.optionValues().stream()
                            .filter(value -> value.price() < 0)
                            .map(value -> value.name() + ":" + value.price())
                            .toList());
            throw new GenericSpecificationException("옵션 값의 가격은 0원 이상이어야 합니다.");
        }
    }
}