package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductOptionResult;
import com.catsriding.store.application.product.result.ProductOptionResult.ProductOptionValueResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "상품 옵션 등록 응답 데이터")
public record ProductOptionResponse(
        @Schema(description = "옵션 ID", example = "98765")
        String optionId,
        @Schema(description = "상품 ID", example = "12345")
        String productId,
        @Schema(description = "옵션명", example = "색상")
        String name,
        @Schema(description = "옵션 타입", example = "SELECT")
        String optionType,
        @Schema(description = "옵션 사용 가능 여부", example = "true")
        boolean usable,
        @Schema(description = "옵션 생성 일시", example = "2025-03-05T14:30:00")
        LocalDateTime createdAt,
        @Schema(description = "옵션 수정 일시", example = "2025-03-06T10:15:00")
        LocalDateTime updatedAt,
        @Schema(description = "옵션 값 목록 (`SELECT` 타입만 해당)")
        List<OptionValue> optionValues
) {

    public static ProductOptionResponse from(ProductOptionResult result) {
        return new ProductOptionResponse(
                String.valueOf(result.id()),
                String.valueOf(result.productId()),
                result.name(),
                result.optionType(),
                result.usable(),
                result.createdAt(),
                result.updatedAt(),
                result.optionValueResults().stream()
                        .map(OptionValue::from)
                        .toList()
        );
    }

    @Schema(description = "상품 옵션 값 응답 데이터")
    private record OptionValue(
            @Schema(description = "옵션 값 ID", example = "67890")
            String optionValueId,
            @Schema(description = "옵션 값 이름", example = "레드")
            String name,
            @Schema(description = "추가 가격 (0 이상)", example = "1000")
            Integer price,
            @Schema(description = "옵션 값 사용 가능 여부", example = "true")
            boolean usable,
            @Schema(description = "옵션 값 생성 일시", example = "2025-03-05T14:30:00")
            LocalDateTime createdAt,
            @Schema(description = "옵션 값 수정 일시", example = "2025-03-06T10:15:00")
            LocalDateTime updatedAt
    ) {

        private static OptionValue from(ProductOptionValueResult result) {
            return new OptionValue(
                    String.valueOf(result.id()),
                    result.name(),
                    result.price(),
                    result.usable(),
                    result.createdAt(),
                    result.updatedAt()
            );
        }
    }
}
