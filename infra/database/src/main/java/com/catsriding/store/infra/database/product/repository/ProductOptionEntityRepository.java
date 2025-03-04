package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionValue;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.catsriding.store.domain.product.repository.ProductOptionRepository;
import com.catsriding.store.infra.database.product.entity.ProductOptionEntity;
import com.catsriding.store.infra.database.product.entity.ProductOptionValueEntity;
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
    public ProductOption save(ProductOption productOption) {
        ProductOptionEntity optionEntity = ProductOptionEntity.from(productOption);
        List<ProductOptionValue> optionValues = productOption.optionValues();

        optionEntity = productOptionJpaRepository.save(optionEntity);
        optionValues = saveOptionValues(optionValues);

        productOption = optionEntity.toDomain(optionValues);

        log.info("save: Successfully save product option - optionId={}, optionName={}, totalValues={}",
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
