package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.shared.exception.GenericSpecificationException;
import com.catsriding.store.domain.shared.spec.AbstractSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OptionNameLengthSpec extends AbstractSpecification<ProductOptionContext> {

    private static final int OPTION_NAME_MAX_LENGTH = 25;
    private static final int OPTION_VALUE_NAME_MAX_LENGTH = 30;

    @Override
    public boolean isSatisfiedBy(ProductOptionContext context) {
        return context.name().length() <= OPTION_NAME_MAX_LENGTH
               && context.optionValues().stream()
                       .allMatch(value -> value.name().length() <= OPTION_VALUE_NAME_MAX_LENGTH);
    }

    @Override
    public void check(ProductOptionContext context) throws GenericSpecificationException {
        if (!isSatisfiedBy(context)) {
            log.warn(
                    "OptionNameLengthSpec failed: Name exceeds max length. optionId={}, productId={}, sellerId={}, optionNameLength={}, invalidValueNames={}",
                    context.optionId(),
                    context.productId(),
                    context.sellerId(),
                    context.name().length(),
                    context.optionValues().stream()
                            .map(ProductOptionValueContext::name)
                            .filter(name -> name.length() > OPTION_VALUE_NAME_MAX_LENGTH)
                            .toList());
            throw new GenericSpecificationException("옵션 이름은 최대 25자, 옵션 값 이름은 최대 30자까지 입력할 수 있습니다.");
        }
    }
}
