package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductDeleteResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "상품 삭제 응답 데이터")
public record ProductDeleteResponse(
        @Schema(description = "상품 ID", example = "9187832627331241")
        String productId,
        @Schema(description = "판매자 ID", example = "836419404700736")
        String sellerId,
        @Schema(description = "상품명", example = "프리미엄 원두 커피")
        String name,
        @Schema(description = "상품 상태", example = "DELETED")
        String statusType,
        @Schema(description = "상품 생성 일시", example = "2025-03-05T14:30:00")
        LocalDateTime createdAt,
        @Schema(description = "상품 수정 일시", example = "2025-03-06T10:15:00")
        LocalDateTime updatedAt
) {

    public static ProductDeleteResponse from(ProductDeleteResult result) {
        return new ProductDeleteResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.statusType(),
                result.updatedAt(),
                result.createdAt()
        );
    }
}
