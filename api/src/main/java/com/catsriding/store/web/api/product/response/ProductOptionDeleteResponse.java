package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductOptionDeleteResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "상품 옵션 삭제 응답 데이터")
public record ProductOptionDeleteResponse(
        @Schema(description = "옵션 ID", example = "9187832627331241")
        String optionId,
        @Schema(description = "상품 ID", example = "9187832627265705")
        String productId,
        @Schema(description = "옵션명", example = "로스팅 정도")
        String name,
        @Schema(description = "옵션 삭제 일시", example = "2025-03-05T18:12:55.426688")
        LocalDateTime updatedAt
) {

    public static ProductOptionDeleteResponse from(ProductOptionDeleteResult result) {
        return new ProductOptionDeleteResponse(
                String.valueOf(result.optionId()),
                String.valueOf(result.productId()),
                result.name(),
                result.updatedAt()
        );
    }
}
