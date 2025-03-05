package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductOwnershipSpec extends AbstractSpecification<ProductOptionContext> {

    private final ProductOption productOption;

    public ProductOwnershipSpec(ProductOption productOption) {
        this.productOption = productOption;
    }

    @Override
    public boolean isSatisfiedBy(ProductOptionContext context) {
        return productOption.productId().id().equals(context.productId());
    }

    @Override
    public void check(ProductOptionContext context) throws GenericSpecificationException {
        if (!isSatisfiedBy(context)) {
            log.warn(
                    "ProductOwnershipSpec failed: Mismatched product or seller. requestedProductId={}, requestedSellerId={}, currentProductId={}",
                    context.productId(),
                    context.sellerId(),
                    productOption.productId().id());
            throw new GenericSpecificationException("해당 옵션을 수정할 권한이 없습니다.");
        }
    }
}
