package com.catsriding.store.web.api.product.request;

import com.catsriding.store.application.product.model.ProductOptionCreate;
import com.catsriding.store.application.product.model.ProductOptionCreate.OptionValue;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.shared.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Schema(description = "상품 옵션 등록 요청 데이터")
@Slf4j
public record ProductOptionCreateRequest(
        @Schema(description = "옵션명", example = "로스팅 정도")
        String name,
        @Schema(description = "옵션 타입: `SELECT` | `INPUT`", example = "SELECT")
        String optionType,
        @Schema(description = "옵션 사용 가능 여부", example = "true")
        boolean usable,
        @Schema(description = "옵션 값 목록: `SELECT` 타입일 경우 최소 1개 이상 필수")
        List<ProductOptionValueCreateRequest> optionValues
) {

    private static final int NAME_MAX_LENGTH = 25;
    private static final int VALUE_NAME_MAX_LENGTH = 30;

    public ProductOptionCreateRequest {
        if (!StringUtils.hasText(name)) {
            log.warn("ProductOptionRequest: product option name is missing");
            throw new InvalidRequestException("상품 옵션의 이름을 입력해주세요.");
        }

        if (name.length() > NAME_MAX_LENGTH) {
            log.warn("ProductOptionRequest: product option name exceeds max length - currentLength={}, maxLength={}",
                    name.length(),
                    NAME_MAX_LENGTH);
            throw new InvalidRequestException("상품 옵션의 이름은 최대 " + NAME_MAX_LENGTH + "자까지 입력할 수 있습니다.");
        }

        if (!StringUtils.hasText(optionType)) {
            log.warn("ProductOptionRequest: option type is missing.");
            throw new InvalidRequestException("옵션 타입을 입력해주세요.");
        }

        if (!optionType.equals("INPUT") && !optionType.equals("SELECT")) {
            log.warn("ProductOptionRequest: invalid option type - optionType={}", optionType);
            throw new InvalidRequestException("옵션 타입이 유효하지 않습니다. 확인 후 다시 시도해주세요.");
        }

        if (optionType.equals("INPUT") && !CollectionUtils.isEmpty(optionValues)) {
            log.warn("ProductOptionRequest: INPUT type should not contain values - optionType={}, values={}",
                    optionType,
                    optionValues.size());
            throw new InvalidRequestException("해당 옵션 타입은 옵션 값을 포함할 수 없습니다.");
        }

        if (optionType.equals("SELECT") && CollectionUtils.isEmpty(optionValues)) {
            log.warn("ProductOptionRequest: SELECT type must contain at least one value - optionType={}", optionType);
            throw new InvalidRequestException("해당 옵션 타입은 최소 하나 이상의 옵션 값이 필요합니다.");
        }
    }

    public ProductOptionCreate toCommand(Long productId, LoginUser user) {
        return new ProductOptionCreate(
                productId,
                user.id(),
                name,
                optionType,
                usable,
                toOptionValues()
        );
    }

    private List<OptionValue> toOptionValues() {
        if (CollectionUtils.isEmpty(optionValues)) return List.of();

        return optionValues.stream()
                .map(ProductOptionValueCreateRequest::toCommand)
                .toList();
    }

    @Schema(description = "상품 옵션 값 등록 데이터")
    public record ProductOptionValueCreateRequest(
            @Schema(description = "옵션 값 이름", example = "미디엄 로스팅")
            String name,
            @Schema(description = "추가 가격: 최소 0원 이상", example = "1000")
            int price,
            @Schema(description = "옵션 값 사용 가능 여부", example = "true")
            boolean usable
    ) {

        public ProductOptionValueCreateRequest {
            if (!StringUtils.hasText(name)) {
                log.warn("ProductOptionValueRequest: option value name is missing");
                throw new InvalidRequestException("옵션 값의 이름을 입력해주세요.");
            }

            if (name.length() > VALUE_NAME_MAX_LENGTH) {
                log.warn("ProductOptionValueRequest: option value name exceeds max length - currLength={}, maxLength={}",
                        name.length(),
                        VALUE_NAME_MAX_LENGTH);
                throw new InvalidRequestException("옵션 값의 이름은 최대 " + VALUE_NAME_MAX_LENGTH + "자까지 입력할 수 있습니다.");
            }

            if (price < 0) {
                log.warn("ProductOptionValueRequest: price must be 0 or greater - price={}", price);
                throw new InvalidRequestException("옵션 값의 가격은 0원 이상이어야 합니다.");
            }
        }

        public OptionValue toCommand() {
            return new OptionValue(name, price, usable);
        }
    }

}
