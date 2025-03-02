package com.catsriding.store.web.api.product.request;

import com.catsriding.store.application.product.model.ProductRegistration;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.shared.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public record ProductRegistrationRequest(
        String name,
        String description,
        int price,
        int deliveryFee,
        String statusType
) {

    public ProductRegistrationRequest {
        validate(name, description, price, deliveryFee, statusType);
    }

    private static void validate(String name, String description, int price, int deliveryFee, String statusType) {
        if (!StringUtils.hasText(name)) {
            log.info("validate: Missing or empty product name");
            throw new InvalidRequestException("상품명을 입력해 주세요.");
        }

        if (!StringUtils.hasText(description)) {
            log.info("validate: Missing or empty product description");
            throw new InvalidRequestException("상품 설명을 입력해 주세요.");
        }

        if (price < 10) {
            log.info("validate: Product price must be at least 10 - price={}", price);
            throw new InvalidRequestException("상품 가격은 최소 10원 이상이어야 합니다. 다시 입력해 주세요.");
        }

        if (deliveryFee < 0) {
            log.info("validate: Delivery fee cannot be negative - deliveryFee={}", deliveryFee);
            throw new InvalidRequestException("배송비는 무료 또는 일정 금액 이상이어야 합니다.");
        }

        if (!statusType.equals("SALE") && !statusType.equals("INACTIVE")) {
            log.info("validate: Invalid status type - statusType={}", statusType);
            throw new InvalidRequestException("상품 상태가 잘못되었습니다. 다시 확인해 주세요.");
        }
    }

    public ProductRegistration toCommand(LoginUser user) {
        return new ProductRegistration(
                user.id(),
                name,
                description,
                price,
                deliveryFee,
                statusType
        );
    }

}
