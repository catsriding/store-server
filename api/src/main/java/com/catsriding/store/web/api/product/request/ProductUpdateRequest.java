package com.catsriding.store.web.api.product.request;

import com.catsriding.store.application.product.model.ProductUpdate;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.shared.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Schema(description = "상품 수정 요청 데이터")
@Slf4j
public record ProductUpdateRequest(
        @Schema(description = "상품명", example = "프리미엄 원두 커피")
        String name,
        @Schema(description = "상품 설명", example = "신선하게 로스팅된 최고급 원두")
        String description,
        @Schema(description = "상품 가격: 최소 10원 이상", example = "15000")
        int price,
        @Schema(description = "배송비: 최소 0원", example = "2500")
        int deliveryFee,
        @Schema(description = "상품 상태: `SALE` | `INACTIVE`", example = "SALE")
        String statusType
) {

    public ProductUpdateRequest {
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

    public ProductUpdate toCommand(Long productId, LoginUser user) {
        return new ProductUpdate(productId, user.id(), name, description, price, deliveryFee, statusType);
    }

}
