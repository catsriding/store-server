package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoDuplicateOptionValueNamesSpec extends AbstractSpecification<UpdateProductOption> {

    @Override
    public boolean isSatisfiedBy(UpdateProductOption updateCommand) {
        if (!updateCommand.optionType().equals(ProductOptionType.SELECT)) return true;

        Set<String> uniqueNames = new HashSet<>();
        return updateCommand.updateOptionValues().stream()
                .map(UpdateProductOption.UpdateProductOptionValue::name)
                .allMatch(uniqueNames::add);
    }

    @Override
    public void check(UpdateProductOption updateCommand) throws GenericSpecificationException {
        if (!isSatisfiedBy(updateCommand)) {
            Set<String> duplicateNames = updateCommand.updateOptionValues().stream()
                    .map(UpdateProductOption.UpdateProductOptionValue::name)
                    .collect(Collectors.toSet());

            log.warn(
                    "NoDuplicateOptionValueNamesSpec failed: Duplicate option values detected. optionId={}, productId={}, sellerId={}, duplicateValues={}",
                    updateCommand.optionId(),
                    updateCommand.productId(),
                    updateCommand.sellerId(),
                    duplicateNames);
            throw new GenericSpecificationException("옵션 값 이름이 중복될 수 없습니다. 확인 후 다시 입력해주세요.");
        }
    }
}