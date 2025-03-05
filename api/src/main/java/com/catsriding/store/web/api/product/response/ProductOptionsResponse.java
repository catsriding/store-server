package com.catsriding.store.web.api.product.response;

import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "상품 옵션 목록 응답 데이터")
public record ProductOptionsResponse(
        @Schema(description = "상품 옵션 목록")
        List<ProductOptionResponse> options,
        @Schema(description = "총 옵션 개수", example = "1")
        int totalCount
) {

    public static ProductOptionsResponse from(List<ProductOptionWithValue> results) {
        Map<Long, List<ProductOptionWithValue>> groupedByOption = groupByOptionId(results);
        List<Long> sortedOptionIds = collectSortedKeys(groupedByOption.keySet());
        List<ProductOptionResponse> options = convertToOptionResponse(sortedOptionIds, groupedByOption);

        return new ProductOptionsResponse(
                options,
                options.size()
        );
    }

    private static Map<Long, List<ProductOptionWithValue>> groupByOptionId(List<ProductOptionWithValue> results) {
        return results.stream()
                .collect(Collectors.groupingBy(ProductOptionWithValue::optionId));
    }

    private static List<Long> collectSortedKeys(Set<Long> longs) {
        return longs.stream()
                .sorted()
                .toList();
    }

    private static List<ProductOptionResponse> convertToOptionResponse(
            List<Long> keys,
            Map<Long, List<ProductOptionWithValue>> groupedByOptionId
    ) {
        return keys.stream()
                .map(optionId -> ProductOptionResponse.from(groupedByOptionId.get(optionId)))
                .toList();
    }

    @Schema(description = "상품 옵션 응답 데이터")
    public record ProductOptionResponse(
            @Schema(description = "옵션 ID", example = "9192132359291049")
            String optionId,
            @Schema(description = "상품 ID", example = "9187832627331241")
            String productId,
            @Schema(description = "옵션명", example = "색상")
            String name,
            @Schema(description = "옵션 타입: `SELECT` | `INPUT`", example = "SELECT")
            String optionType,
            @Schema(description = "옵션 사용 가능 여부", example = "true")
            boolean usable,
            @Schema(description = "옵션 생성 일시", example = "2025-03-05T14:30:00")
            LocalDateTime createdAt,
            @Schema(description = "옵션 수정 일시", example = "2025-03-06T10:15:00")
            LocalDateTime updatedAt,
            @Schema(description = "옵션 값 목록: `SELECT` 타입인 경우")
            List<ProductOptionValueResponse> optionValues
    ) {

        private static ProductOptionResponse from(List<ProductOptionWithValue> optionWithValues) {
            ProductOptionWithValue first = optionWithValues.getFirst();

            List<ProductOptionValueResponse> optionValues = optionWithValues.stream()
                    .filter(result -> result.optionValueId() != null)
                    .map(ProductOptionValueResponse::from)
                    .sorted(Comparator.comparing(v -> v.optionValueId))
                    .toList();

            return new ProductOptionResponse(
                    String.valueOf(first.optionId()),
                    String.valueOf(first.productId()),
                    first.name(),
                    first.optionType().name(),
                    first.usable(),
                    first.createdAt(),
                    first.updatedAt(),
                    optionValues
            );
        }
    }

    @Schema(description = "상품 옵션 값 응답 데이터")
    public record ProductOptionValueResponse(
            @Schema(description = "옵션 값 ID", example = "9192251947352233")
            String optionValueId,
            @Schema(description = "옵션 값 이름", example = "레드")
            String name,
            @Schema(description = "옵션 추가 가격", example = "1000")
            Integer price,
            @Schema(description = "옵션 값 사용 가능 여부", example = "true")
            boolean usable,
            @Schema(description = "옵션 값 생성 일시", example = "2025-03-05T14:30:00")
            LocalDateTime createdAt,
            @Schema(description = "옵션 값 수정 일시", example = "2025-03-06T10:15:00")
            LocalDateTime updatedAt
    ) {

        public static ProductOptionValueResponse from(ProductOptionWithValue result) {
            return new ProductOptionValueResponse(
                    String.valueOf(result.optionValueId()),
                    result.optionValueName(),
                    result.optionValuePrice(),
                    result.optionValueUsable(),
                    result.optionValueCreatedAt(),
                    result.optionValueUpdatedAt()
            );
        }
    }
}
