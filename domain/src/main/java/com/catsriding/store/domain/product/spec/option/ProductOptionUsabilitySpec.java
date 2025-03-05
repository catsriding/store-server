package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductOptionUsabilitySpec extends AbstractSpecification<UpdateProductOption> {

    @Override
    public boolean isSatisfiedBy(UpdateProductOption updateOption) {
        return updateOption.usable() || !updateOption.updateOptionValues().isEmpty();
    }

    @Override
    public void check(UpdateProductOption updateOption) throws GenericSpecificationException {
        if (!isSatisfiedBy(updateOption)) {
            log.warn(
                    "ProductOptionUsabilitySpec failed: A disabled option must have no option values. optionId={}, productId={}, sellerId={}",
                    updateOption.optionId(),
                    updateOption.productId(),
                    updateOption.sellerId());
            throw new GenericSpecificationException("비활성화된 옵션은 옵션 값을 가질 수 없습니다.");
        }
    }
}