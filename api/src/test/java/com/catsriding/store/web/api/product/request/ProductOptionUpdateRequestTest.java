package com.catsriding.store.web.api.product.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.catsriding.store.application.product.model.ProductOptionUpdate;
import com.catsriding.store.application.product.model.ProductOptionUpdate.OptionValue;
import com.catsriding.store.domain.user.UserRoleType;
import com.catsriding.store.web.api.product.request.ProductOptionUpdateRequest.ProductOptionValueRequest;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.shared.LoginUser;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ProductOptionUpdateRequestTest {

    @Test
    @DisplayName("✅ 유효한 옵션 업데이트 요청은 검증 통과")
    void shouldPassValidationForValidProductOptionUpdateRequest() {
        // Given
        String name = "색상";
        String optionType = "SELECT";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("블랙", 1000, true)
        );

        // When & Then
        assertThatNoException().isThrownBy(() -> new ProductOptionUpdateRequest(name,
                optionType,
                usable,
                optionValues));
    }

    @Test
    @DisplayName("🚫 옵션 이름이 비어 있으면 예외 발생")
    void shouldThrowExceptionWhenOptionNameIsEmpty() {
        // Given
        String name = "";
        String optionType = "SELECT";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("화이트", 2000, true)
        );

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(name, optionType, usable, optionValues))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("상품 옵션의 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("🚫 옵션 이름이 25자를 초과하면 예외 발생")
    void shouldThrowExceptionWhenOptionNameExceedsMaxLength() {
        // Given
        String longName = RandomString.make(26);
        String optionType = "SELECT";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("블랙", 1000, true)
        );

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(longName, optionType, usable, optionValues))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("상품 옵션의 이름은 최대 25자까지 입력할 수 있습니다.");
    }

    @Test
    @DisplayName("🚫 옵션 타입이 비어 있으면 예외 발생")
    void shouldThrowExceptionWhenOptionTypeIsEmpty() {
        // Given
        String name = "색상";
        String optionType = "";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("화이트", 2000, true)
        );

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(name, optionType, usable, optionValues))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("옵션 타입을 입력해주세요.");
    }

    @Test
    @DisplayName("🚫 옵션 타입이 잘못된 값이면 예외 발생")
    void shouldThrowExceptionWhenInvalidOptionType() {
        // Given
        String name = "색상";
        String optionType = "UNKNOWN";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("화이트", 2000, true)
        );

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(name, optionType, usable, optionValues))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("옵션 타입이 유효하지 않습니다. 확인 후 다시 시도해주세요.");
    }

    @Test
    @DisplayName("🚫 INPUT 타입인데 옵션 값이 존재하면 예외 발생")
    void shouldThrowExceptionWhenInputTypeContainsValues() {
        // Given
        String name = "사이즈";
        String optionType = "INPUT";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("L", 0, true)
        );

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(name, optionType, usable, optionValues))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("해당 옵션 타입은 옵션 값을 포함할 수 없습니다.");
    }

    @Test
    @DisplayName("🚫 SELECT 타입인데 옵션 값이 없으면 예외 발생")
    void shouldThrowExceptionWhenSelectTypeHasNoValues() {
        // Given
        String name = "색상";
        String optionType = "SELECT";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of();

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(name, optionType, usable, optionValues))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("해당 옵션 타입은 최소 하나 이상의 옵션 값이 필요합니다.");
    }

    @Test
    @DisplayName("🚫 옵션 값의 가격이 음수이면 예외 발생")
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Given
        int negativePrice = -1;
        String name = "색상";
        String optionType = "SELECT";
        boolean usable = true;

        // When & Then
        assertThatThrownBy(() -> new ProductOptionUpdateRequest(
                name,
                optionType,
                usable,
                List.of(new ProductOptionValueRequest("L", negativePrice, true))))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("옵션 값의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("✅ 요청 데이터가 유효하면 커맨드 객체 변환 성공")
    void shouldConvertToCommandSuccessfully() {
        // Given
        LoginUser user = new LoginUser(1L, "testUser", UserRoleType.ROLE_USER);
        Long productId = 2L;
        Long optionId = 3L;
        String name = "색상";
        String optionType = "SELECT";
        boolean usable = true;
        List<ProductOptionValueRequest> optionValues = List.of(
                new ProductOptionValueRequest("레드", 1000, true),
                new ProductOptionValueRequest("블루", 2000, true)
        );

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(name, optionType, usable, optionValues);

        // When
        ProductOptionUpdate command = request.toCommand(productId, optionId, user);

        // Then
        assertThat(command.optionId()).isEqualTo(optionId);
        assertThat(command.productId()).isEqualTo(productId);
        assertThat(command.sellerId()).isEqualTo(user.id());
        assertThat(command.name()).isEqualTo(name);
        assertThat(command.optionType()).isEqualTo(optionType);
        assertThat(command.usable()).isEqualTo(usable);
        assertThat(command.optionValues()).hasSize(2);
        assertThat(command.optionValues()).extracting(OptionValue::name).containsExactly("레드", "블루");
    }
}