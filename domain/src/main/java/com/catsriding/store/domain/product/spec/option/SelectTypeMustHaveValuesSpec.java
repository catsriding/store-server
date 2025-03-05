package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectTypeMustHaveValuesSpec extends AbstractSpecification<ProductOptionContext> {

    @Override
    public boolean isSatisfiedBy(ProductOptionContext context) {
        return !context.optionType().equals(ProductOptionType.SELECT)
               || !context.optionValues().isEmpty();
    }

    @Override
    public void check(ProductOptionContext context) throws GenericSpecificationException {
        if (!isSatisfiedBy(context)) {
            log.warn(
                    "SelectTypeMustHaveValuesSpec failed: SELECT type requires at least one option value. optionId={}, productId={}, sellerId={}, providedValues={}",
                    context.optionId(),
                    context.productId(),
                    context.sellerId(),
                    context.optionValues().size());
            throw new GenericSpecificationException("SELECT 타입 옵션에는 최소 하나 이상의 옵션 값이 필요합니다.");
        }
    }
}