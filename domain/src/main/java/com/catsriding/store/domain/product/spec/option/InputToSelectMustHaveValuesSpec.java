package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputToSelectMustHaveValuesSpec extends AbstractSpecification<ProductOptionContext> {

    @Override
    public boolean isSatisfiedBy(ProductOptionContext updateOption) {
        if (!updateOption.optionType().equals(ProductOptionType.SELECT)) return true;

        return !updateOption.optionValues().isEmpty();
    }

    @Override
    public void check(ProductOptionContext context) throws GenericSpecificationException {
        if (!isSatisfiedBy(context)) {
            log.warn(
                    "InputToSelectMustHaveValuesSpec failed: Switching to SELECT requires at least one option value. optionId={}, productId={}, sellerId={}",
                    context.optionId(),
                    context.productId(),
                    context.sellerId());
            throw new GenericSpecificationException("SELECT 타입으로 변경하려면 최소 하나 이상의 옵션 값이 필요합니다.");
        }
    }
}