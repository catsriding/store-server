package com.catsriding.store.web.api.product;

import static com.catsriding.store.web.shared.ApiResponse.failure;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.catsriding.store.application.product.exception.ProductOptionLimitExceededException;
import com.catsriding.store.application.product.exception.ProductUnavailableException;
import com.catsriding.store.web.support.ApiExceptionLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ProductOptionControllerAdvice {

    @ExceptionHandler(ProductUnavailableException.class)
    public ResponseEntity<?> handleProductUnavailableException(
            ProductUnavailableException e,
            HttpServletRequest request
    ) {
        ApiExceptionLogger.logWarning("handleProductUnavailableException", "Bad request", e, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(failure(null, e.getMessage()));
    }

    @ExceptionHandler(ProductOptionLimitExceededException.class)
    public ResponseEntity<?> handleProductOptionLimitExceededException(
            ProductOptionLimitExceededException e,
            HttpServletRequest request
    ) {
        ApiExceptionLogger.logWarning("handleProductOptionLimitExceededException", "Bad request", e, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(failure(null, e.getMessage()));
    }

}
