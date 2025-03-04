package com.catsriding.store.application.product;

import com.catsriding.store.application.product.exception.ProductOptionLimitExceededException;
import com.catsriding.store.application.product.exception.ProductUnavailableException;
import com.catsriding.store.application.product.model.ProductOptionCreate;
import com.catsriding.store.application.product.model.ProductOptionDelete;
import com.catsriding.store.application.product.result.ProductOptionDeleteResult;
import com.catsriding.store.application.product.result.ProductOptionResult;
import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.catsriding.store.domain.product.repository.ProductOptionRepository;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.shared.ClockHolder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductOptionService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ClockHolder clockHolder;

    public ProductOptionService(
            ProductRepository productRepository,
            ProductOptionRepository productOptionRepository,
            ClockHolder clockHolder
    ) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.clockHolder = clockHolder;
    }

    public List<ProductOptionWithValue> retrieveOptions(ProductOptionIdentifier cond) {
        return productOptionRepository.loadProductOptions(cond);
    }

    public ProductOptionResult createProductOption(ProductOptionCreate command) {
        ProductOption productOption = ProductOption.from(command.toNewOption(), clockHolder);
        ProductOptionIdentifier identifier = productOption.toIdentifier();

        validateProductExistence(identifier);
        validateProductOptionLimit(identifier);

        productOption = productOptionRepository.saveWithOptionValues(productOption);

        log.info("createProductOption: Successfully created product option - optionId={}, productId={}, sellerId={}",
                identifier.optionId().id(),
                identifier.productId(),
                identifier.sellerId());

        return ProductOptionResult.from(productOption);
    }

    public ProductOptionDeleteResult deleteProductOption(ProductOptionDelete command) {
        ProductOptionIdentifier identifier = command.toIdentifier();
        validateProductExistence(identifier);

        ProductOption productOption = productOptionRepository.loadProductOption(identifier);
        productOption = productOption.deleteProductOption(clockHolder);
        productOption = productOptionRepository.saveWithoutOptionValues(productOption);

        log.info("deleteProductOption: Successfully deleted product option - optionId={}, productId={}, sellerId={}",
                identifier.optionId().id(),
                identifier.productId().id(),
                identifier.sellerId().id());

        return ProductOptionDeleteResult.from(productOption);
    }

    private void validateProductExistence(ProductOptionIdentifier identifier) {
        if (!productRepository.existsBy(identifier)) {
            log.warn("validateProductExistence: Product does not exist. productId={}, sellerId={}",
                    identifier.productId().id(),
                    identifier.sellerId().id());
            throw new ProductUnavailableException("해당 상품을 찾을 수 없습니다. 상품이 삭제되었거나 유효하지 않은 상품일 수 있습니다.");
        }
    }

    private void validateProductOptionLimit(ProductOptionIdentifier identifier) {
        int optionCount = productOptionRepository.countBy(identifier);
        if (optionCount > 2) {
            log.warn(
                    "validateProductOptionLimit: Product option limit exceeded - optionCount={}, productId={}, sellerId={}",
                    optionCount,
                    identifier.productId().id(),
                    identifier.sellerId().id()
            );
            throw new ProductOptionLimitExceededException("상품 옵션의 최대 개수를 초과했습니다. 하나의 상품에는 최대 3개의 옵션만 추가할 수 있습니다.");
        }
    }

}
