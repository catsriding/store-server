package com.catsriding.store.domain.shared;

import java.util.List;

public record PagedData<T>(
        List<T> content,
        long totalCount,
        int totalPages,
        int pageNumber,
        int pageSize,
        boolean isFirst,
        boolean isLast,
        boolean hasNext
) {

}
