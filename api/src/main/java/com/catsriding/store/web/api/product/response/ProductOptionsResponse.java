package com.catsriding.store.web.api.product.response;

import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductOptionsResponse(
        List<ProductOptionResponse> options,
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

    private record ProductOptionResponse(
            String optionId,
            String productId,
            String name,
            String optionType,
            boolean usable,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<OptionValue> optionValues
    ) {

        private static ProductOptionResponse from(List<ProductOptionWithValue> optionWithValues) {
            ProductOptionWithValue first = optionWithValues.getFirst();

            List<OptionValue> optionValues = optionWithValues.stream()
                    .filter(result -> result.optionValueId() != null)
                    .map(OptionValue::from)
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

    private record OptionValue(
            String optionValueId,
            String name,
            Integer price,
            boolean usable,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        private static OptionValue from(ProductOptionWithValue result) {
            return new OptionValue(
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
