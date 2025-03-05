package com.catsriding.store.domain.product.spec;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductNonDeletedStateSpec extends AbstractSpecification<Product> {

    @Override
    public boolean isSatisfiedBy(Product product) {
        return isNotDeletedStatus(product) && isNotDeleted(product);
    }

    @Override
    public void check(Product product) {
        if (!isNotDeletedStatus(product)) {
            log.info("Product updatable validation failed: Cannot update deleted product - productId={}",
                    product.productId().id());
            throw new GenericSpecificationException("삭제된 상품은 수정할 수 없습니다.");
        }

        if (!isNotDeleted(product)) {
            log.info("Product updatable validation failed: Product marked as deleted - productId={}",
                    product.productId().id());
            throw new GenericSpecificationException("이 상품은 삭제 상태로 수정할 수 없습니다.");
        }
    }

    private boolean isNotDeletedStatus(Product product) {
        return product.status() != ProductStatusType.DELETED;
    }

    private boolean isNotDeleted(Product product) {
        return !product.isDeleted();
    }
}
