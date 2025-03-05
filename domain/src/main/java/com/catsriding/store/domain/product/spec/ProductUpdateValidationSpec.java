package com.catsriding.store.domain.product.spec;

import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.model.UpdateProduct;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class ProductUpdateValidationSpec extends AbstractSpecification<UpdateProduct> {

    @Override
    public boolean isSatisfiedBy(UpdateProduct product) {
        return hasValidId(product)
               && hasValidName(product)
               && hasValidPrice(product)
               && hasValidDeliveryFee(product)
               && hasValidStatus(product);
    }

    @Override
    public void check(UpdateProduct product) {
        if (!hasValidId(product)) {
            log.info("Product update validation failed: missing or invalid ID");
            throw new GenericSpecificationException("상품 ID는 필수 입력 항목입니다.");
        }

        if (!hasValidUserId(product)) {
            log.info("Product update validation failed: missing or invalid user ID");
            throw new GenericSpecificationException("유저 ID는 필수 입력 항목입니다.");
        }

        if (!hasValidName(product)) {
            log.info("Product update validation failed: missing or empty name - name={}", product.name());
            throw new GenericSpecificationException("상품명은 필수 입력 항목입니다.");
        }

        if (!hasValidPrice(product)) {
            log.info("Product update validation failed: price must be at least 10 - price={}", product.price());
            throw new GenericSpecificationException("상품 가격은 최소 10원 이상이어야 합니다.");
        }

        if (!hasValidDeliveryFee(product)) {
            log.info("Product update validation failed: delivery fee cannot be negative - deliveryFee={}",
                    product.deliveryFee());
            throw new GenericSpecificationException("배송비는 0원 이상이어야 합니다.");
        }

        if (!hasValidStatus(product)) {
            log.info("Product update validation failed: invalid status type - status={}", product.statusType());
            throw new GenericSpecificationException("상품 상태가 유효하지 않습니다.");
        }
    }

    private boolean hasValidId(UpdateProduct product) {
        return product.productId() != null && product.productId().id() > 0;
    }

    private boolean hasValidUserId(UpdateProduct product) {
        return product.userId() != null && product.userId().id() > 0;
    }

    private boolean hasValidName(UpdateProduct product) {
        return StringUtils.hasText(product.name());
    }

    private boolean hasValidPrice(UpdateProduct product) {
        return product.price() >= 10;
    }

    private boolean hasValidDeliveryFee(UpdateProduct product) {
        return product.deliveryFee() >= 0;
    }

    private boolean hasValidStatus(UpdateProduct product) {
        return product.statusType() == ProductStatusType.SALE || product.statusType() == ProductStatusType.INACTIVE;
    }
}
