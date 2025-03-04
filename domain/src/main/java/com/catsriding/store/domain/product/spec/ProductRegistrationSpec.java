package com.catsriding.store.domain.product.spec;

import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.model.NewProduct;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class ProductRegistrationSpec extends AbstractSpecification<NewProduct> {

    @Override
    public boolean isSatisfiedBy(NewProduct product) {
        return hasValidName(product)
               && hasValidPrice(product)
               && hasValidDeliveryFee(product)
               && hasValidStatus(product);
    }

    @Override
    public void check(NewProduct product) {
        if (!hasValidName(product)) {
            log.info("Product validation failed: missing or empty name - name={}", product.name());
            throw new GenericSpecificationException("상품명은 필수 입력 항목입니다.");
        }

        if (!hasValidPrice(product)) {
            log.info("Product validation failed: price must be at least 10 - price={}", product.price());
            throw new GenericSpecificationException("상품 가격은 최소 10원 이상이어야 합니다.");
        }

        if (!hasValidDeliveryFee(product)) {
            log.info("Product validation failed: delivery fee cannot be negative - deliveryFee={}",
                    product.deliveryFee());
            throw new GenericSpecificationException("배송비는 0원 이상이어야 합니다.");
        }

        if (!hasValidStatus(product)) {
            log.info("Product validation failed: invalid status type - status={}", product.statusType());
            throw new GenericSpecificationException("상품 상태가 유효하지 않습니다.");
        }
    }

    private boolean hasValidName(NewProduct product) {
        return StringUtils.hasText(product.name());
    }

    private boolean hasValidPrice(NewProduct product) {
        return product.price() >= 10;
    }

    private boolean hasValidDeliveryFee(NewProduct product) {
        return product.deliveryFee() >= 0;
    }

    private boolean hasValidStatus(NewProduct product) {
        return product.statusType() == ProductStatusType.SALE || product.statusType() == ProductStatusType.INACTIVE;
    }
}
