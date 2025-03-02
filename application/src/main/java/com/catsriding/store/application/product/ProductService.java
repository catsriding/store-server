package com.catsriding.store.application.product;

import com.catsriding.store.application.product.model.ProductRegistration;
import com.catsriding.store.application.product.result.ProductRegistrationResult;
import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.shared.ClockHolder;
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

    public ProductRegistrationResult registerNewProduct(ProductRegistration command) {
        Product product = Product.from(command.toNewProduct(), clockHolder);
        product = productRepository.save(product);

        log.info("registerNewProduct: Successfully register new product - productId={}, sellerId={}",
                product.productId().id(),
                product.sellerId().id());

        return ProductRegistrationResult.from(product);
    }

}
