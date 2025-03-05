package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductRegisterResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "상품 등록 응답 데이터")
public record ProductRegisterResponse(
        @Schema(description = "등록된 상품 ID", example = "9187832627265705")
        String productId,
        @Schema(description = "판매자 ID", example = "9187832627331241")
        String sellerId,
        @Schema(description = "상품명", example = "프리미엄 원두 커피")
        String name,
        @Schema(description = "상품 상태: `SALE` | `INACTIVE`", example = "SALE")
        String statusType,
        @Schema(description = "상품 등록 일시", example = "2025-03-06T12:34:56.789")
        LocalDateTime createdAt
) {

    public static ProductRegisterResponse from(ProductRegisterResult result) {
        return new ProductRegisterResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.statusType(),
                result.createdAt()
        );
    }
}
