package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductDetailsResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "상품 정보 조회 응답 데이터")
public record ProductDetailsResponse(
        @Schema(description = "상품 ID", example = "9187832627331241")
        String productId,
        @Schema(description = "판매자 ID", example = "836419404700736")
        String sellerId,
        @Schema(description = "상품명", example = "프리미엄 원두 커피")
        String name,
        @Schema(description = "상품 설명", example = "신선하게 로스팅된 최고급 원두")
        String description,
        @Schema(description = "상품 가격", example = "15000")
        Integer price,
        @Schema(description = "배송비", example = "2500")
        Integer deliveryFee,
        @Schema(description = "상품 상태", example = "SALE")
        String statusType,
        @Schema(description = "상품 생성 일시", example = "2025-03-05T12:00:00.000")
        LocalDateTime createdAt,
        @Schema(description = "상품 수정 일시", example = "2025-03-05T12:30:00.000")
        LocalDateTime updatedAt
) {

    public static ProductDetailsResponse from(ProductDetailsResult result) {
        return new ProductDetailsResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.description(),
                result.price(),
                result.deliveryFee(),
                result.statusType(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
