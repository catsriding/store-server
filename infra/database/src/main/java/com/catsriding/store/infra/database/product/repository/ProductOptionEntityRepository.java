package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionValue;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.catsriding.store.domain.product.repository.ProductOptionRepository;
import com.catsriding.store.infra.database.product.entity.ProductOptionEntity;
import com.catsriding.store.infra.database.product.entity.ProductOptionValueEntity;
import com.catsriding.store.infra.database.shared.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Repository
public class ProductOptionEntityRepository implements ProductOptionRepository {

    private final ProductOptionJpaRepository productOptionJpaRepository;
    private final ProductOptionValueJpaRepository productOptionValueJpaRepository;

    public ProductOptionEntityRepository(
            ProductOptionJpaRepository optionJpaRepository,
            ProductOptionValueJpaRepository productOptionValueJpaRepository
    ) {
        this.productOptionJpaRepository = optionJpaRepository;
        this.productOptionValueJpaRepository = productOptionValueJpaRepository;
    }

    @Override
    @Transactional
    public ProductOption saveWithOptionValues(ProductOption productOption) {
        ProductOptionEntity optionEntity = ProductOptionEntity.from(productOption);
        List<ProductOptionValue> optionValues = productOption.optionValues();

        optionEntity = productOptionJpaRepository.save(optionEntity);
        optionValues = saveOptionValues(optionValues);

        productOption = optionEntity.toDomain(optionValues);

        log.info("saveWithOptionValues: Successfully save product option - optionId={}, optionName={}, totalValues={}",
                productOption.id(),
                productOption.name(),
                optionValues.size());

        return productOption;
    }

    private List<ProductOptionValue> saveOptionValues(List<ProductOptionValue> values) {
        if (CollectionUtils.isEmpty(values)) return new ArrayList<>();

        return values.stream()
                .map(ProductOptionValueEntity::newEntity)
                .map(productOptionValueJpaRepository::save)
                .map(ProductOptionValueEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public ProductOption saveWithoutOptionValues(ProductOption productOption) {
        ProductOptionEntity entity = ProductOptionEntity.from(productOption);
        entity = productOptionJpaRepository.save(entity);

        log.info("saveWithoutOptionValues: Successfully save product option - optionId={}, productId={}",
                productOption.id(),
                productOption.productId().id());

        return entity.toDomain();
    }

    @Override
    @Transactional
    public ProductOption updateWithOptionValues(ProductOption productOption) {
        ProductOptionEntity optionEntity = ProductOptionEntity.from(productOption);
        List<ProductOptionValue> optionValues = productOption.optionValues();

        optionEntity = productOptionJpaRepository.save(optionEntity);
        optionValues = updateOptionValues(optionValues);

        productOption = optionEntity.toDomain(optionValues);

        log.info(
                "updateWithOptionValues: Successfully updated product option - optionId={}, productId={}, optionValueIds={}",
                productOption.id(),
                productOption.productId().id(),
                productOption.optionValueIds());

        return productOption;
    }

    private List<ProductOptionValue> updateOptionValues(List<ProductOptionValue> values) {
        if (CollectionUtils.isEmpty(values)) return new ArrayList<>();

        return values.stream()
                .map(ProductOptionValueEntity::updateEntity)
                .map(productOptionValueJpaRepository::save)
                .filter(ProductOptionValueEntity::isActive)
                .map(ProductOptionValueEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOption loadProductOption(ProductOptionIdentifier identifier) {
        return productOptionJpaRepository.fetchBy(identifier)
                .map(ProductOptionEntity::toDomain)
                .orElseThrow(() -> {
                    log.warn("loadProductOption: Does not found product option - optionId={}, productId={}, sellerId={}",
                            identifier.optionId().id(),
                            identifier.productId().id(),
                            identifier.sellerId().id());
                    return new DataNotFoundException("요청한 상품 옵션을 찾을 수 없습니다. 확인 후 다시 확인해주세요.");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOption loadProductOptionWithValues(ProductOptionIdentifier identifier) {
        List<ProductOptionValue> optionValues = productOptionValueJpaRepository.fetchAllBy(identifier)
                .stream()
                .map(ProductOptionValueEntity::toDomain)
                .toList();
        return productOptionJpaRepository.fetchBy(identifier)
                .map(entity -> entity.toDomain(optionValues))
                .orElseThrow(() -> {
                    log.warn(
                            "loadProductOptionWithValues: Does not found product option - optionId={}, productId={}, sellerId={}",
                            identifier.optionId().id(),
                            identifier.productId().id(),
                            identifier.sellerId().id());
                    return new DataNotFoundException("요청한 상품 옵션을 찾을 수 없습니다. 확인 후 다시 확인해주세요.");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionWithValue> loadProductOptions(ProductOptionIdentifier identifier) {
        return productOptionJpaRepository.fetchOptionsWithValues(identifier);
    }

    @Override
    @Transactional(readOnly = true)
    public int countBy(ProductOptionIdentifier identifier) {
        return productOptionJpaRepository.countBy(identifier);
    }

}
