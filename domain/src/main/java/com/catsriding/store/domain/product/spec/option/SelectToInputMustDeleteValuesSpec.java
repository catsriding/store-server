package com.catsriding.store.domain.product.spec.option;

import static com.catsriding.store.domain.product.ProductOptionType.INPUT;
import static com.catsriding.store.domain.product.ProductOptionType.SELECT;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectToInputMustDeleteValuesSpec extends AbstractSpecification<ProductOptionContext> {

    private final ProductOption productOption;

    public SelectToInputMustDeleteValuesSpec(ProductOption productOption) {
        this.productOption = productOption;
    }

    @Override
    public boolean isSatisfiedBy(ProductOptionContext context) {
        if (productOption.optionType().equals(SELECT) && context.optionType().equals(INPUT)) {
            return !productOption.optionValues().isEmpty() && context.optionValues().isEmpty();
        }
        return true;
    }

    @Override
    public void check(ProductOptionContext context) throws GenericSpecificationException {
        if (!isSatisfiedBy(context)) {
            log.warn(
                    "SelectToInputMustDeleteValuesSpec failed: When changing from SELECT to INPUT, existing values must exist in the domain, and no new values should be present in the update request. optionId={}, productId={}, sellerId={}, existingValues={}, updateValues={}",
                    context.optionId(),
                    context.productId(),
                    context.sellerId(),
                    productOption.optionValues().size(),
                    context.optionValues().size());
            throw new GenericSpecificationException(
                    "SELECT 타입에서 INPUT 타입으로 변경할 때, 기존 옵션 값이 존재해야 하며, 새로운 옵션 값은 포함될 수 없습니다.");
        }
    }
}
