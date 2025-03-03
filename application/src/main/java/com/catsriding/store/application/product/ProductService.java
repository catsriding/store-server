package com.catsriding.store.application.product;

import com.catsriding.store.application.product.model.ProductDelete;
import com.catsriding.store.domain.product.model.ProductPageCond;
import com.catsriding.store.application.product.model.ProductRegistration;
import com.catsriding.store.application.product.model.ProductUpdate;
import com.catsriding.store.application.product.result.ProductDeleteResult;
import com.catsriding.store.application.product.result.ProductRegistrationResult;
import com.catsriding.store.application.product.result.ProductUpdateResult;
import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.model.ProductSummary;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.shared.PagedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ClockHolder clockHolder;

    public ProductService(ProductRepository productRepository, ClockHolder clockHolder) {
        this.productRepository = productRepository;
        this.clockHolder = clockHolder;
    }

    public PagedData<ProductSummary> retrievePagedProducts(ProductPageCond cond) {
        return productRepository.loadPagedProducts(cond);
    }

    public ProductRegistrationResult registerNewProduct(ProductRegistration command) {
        Product product = Product.from(command.toNewProduct(), clockHolder);
        product = productRepository.save(product);

        log.info("registerNewProduct: Successfully register new product - productId={}, sellerId={}",
                product.productId().id(),
                product.sellerId().id());

        return ProductRegistrationResult.from(product);
    }

    public ProductUpdateResult updateProduct(ProductUpdate command) {
        Product product = productRepository.loadProduct(command.toIdentifier());

        product = product.update(command.toUpdateProduct(), clockHolder);
        product = productRepository.save(product);

        log.info("updateProduct: Successfully updated product - productId={}, sellerId={}",
                product.productId().id(),
                product.sellerId().id());

        return ProductUpdateResult.from(product);
    }

    public ProductDeleteResult deleteProduct(ProductDelete command) {
        Product product = productRepository.loadProduct(command.toIdentifier());

        product = product.delete(clockHolder);
        product = productRepository.save(product);

        log.info("deleteProduct: Successfully deleted product - productId={}, sellerId={}",
                product.productId().id(),
                product.sellerId().id());

        return ProductDeleteResult.from(product);
    }
}
