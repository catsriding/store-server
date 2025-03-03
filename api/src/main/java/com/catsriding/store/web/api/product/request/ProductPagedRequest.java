package com.catsriding.store.web.api.product.request;

import com.catsriding.store.domain.product.model.ProductPageCond;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.shared.LoginUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ProductPagedRequest(
        int pageNumber,
        int pageSize,
        Long userId
) {

    public ProductPagedRequest {
        validate(pageNumber, pageSize);
    }

    private static void validate(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            log.info("validate: Invalid page number - pageNumber={}", pageNumber);
            throw new InvalidRequestException("첫 번째 페이지부터 조회할 수 있습니다. 페이지 번호를 다시 확인해 주세요.");
        }

        if (pageSize < 1) {
            log.info("validate: Invalid page size: pageSize={}", pageSize);
            throw new InvalidRequestException("한 번에 조회할 수 있는 항목 개수를 다시 확인해 주세요.");
        }
    }

    public static ProductPagedRequest from(int pageNumber, int pageSize, LoginUser user) {
        return new ProductPagedRequest(pageNumber, pageSize, user.id());
    }

    public ProductPageCond toCond() {
        return new ProductPageCond(userId, pageNumber, pageSize);
    }
}
