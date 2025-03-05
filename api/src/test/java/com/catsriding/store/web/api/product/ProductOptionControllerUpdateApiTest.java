package com.catsriding.store.web.api.product;

import static java.util.Locale.KOREA;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionId;
import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.ProductOptionValue;
import com.catsriding.store.domain.product.ProductOptionValueId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.repository.ProductOptionRepository;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.web.api.product.request.ProductOptionUpdateRequest;
import com.catsriding.store.web.api.product.request.ProductOptionUpdateRequest.ProductOptionValueRequest;
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class ProductOptionControllerUpdateApiTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductOptionRepository productOptionRepository;

    private Product createProduct(User user) {
        Product product = Product.builder()
                .id(ProductId.withoutId())
                .sellerId(user.userId())
                .name("커피 원두")
                .description("고품질 원두")
                .price(15000)
                .deliveryFee(2500)
                .statusType(ProductStatusType.SALE)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return productRepository.save(product);
    }

    private ProductOption createSelectProductOption(Product product) {
        ProductOptionId optionId = ProductOptionId.withoutId();
        ProductOption option = ProductOption.builder()
                .id(optionId)
                .productId(product.productId())
                .name("색상")
                .optionType(ProductOptionType.SELECT)
                .usable(true)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .optionValues(List.of(
                        ProductOptionValue.builder()
                                .id(ProductOptionValueId.withoutId())
                                .optionId(optionId)
                                .name("레드")
                                .price(1000)
                                .usable(true)
                                .isDeleted(false)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build(),
                        ProductOptionValue.builder()
                                .id(ProductOptionValueId.withoutId())
                                .optionId(optionId)
                                .name("블루")
                                .price(1500)
                                .usable(true)
                                .isDeleted(false)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ))
                .build();
        return productOptionRepository.saveWithOptionValues(option);
    }

    private ProductOption createInputProductOption(Product product) {
        ProductOptionId optionId = ProductOptionId.withoutId();
        ProductOption option = ProductOption.builder()
                .id(optionId)
                .productId(product.productId())
                .name("포장 방식")
                .optionType(ProductOptionType.INPUT)
                .usable(true)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .optionValues(List.of())
                .build();
        return productOptionRepository.saveWithoutOptionValues(option);
    }

    @Test
    @DisplayName("product option update api")
    void docsProductOptionUpdateApi() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption productOption = createSelectProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "색상 변경",
                "SELECT",
                true,
                List.of(
                        new ProductOptionValueRequest("그린", 1400, true),
                        new ProductOptionValueRequest("옐로우", 1100, false),
                        new ProductOptionValueRequest("퍼플", 900, true)
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                productOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(productOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("색상 변경"))
                .andExpect(jsonPath("$.data.optionType").value("SELECT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())

                // 옵션 값 개수 검증 (기존 옵션 값은 삭제되고, 새 값 3개만 존재해야 함)
                .andExpect(jsonPath("$.data.optionValues.length()").value(3))

                // 옵션 값 상세 검증
                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].name").value("그린"))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(1400))
                .andExpect(jsonPath("$.data.optionValues[0].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.optionValues[1].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].name").value("옐로우"))
                .andExpect(jsonPath("$.data.optionValues[1].price").value(1100))
                .andExpect(jsonPath("$.data.optionValues[1].usable").value(false))
                .andExpect(jsonPath("$.data.optionValues[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.optionValues[2].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[2].name").value("퍼플"))
                .andExpect(jsonPath("$.data.optionValues[2].price").value(900))
                .andExpect(jsonPath("$.data.optionValues[2].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[2].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[2].updatedAt").isNotEmpty());

        actions.andDo(restDocs.document(
                requestHeaders(
                        headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                        headerWithName(ACCEPT).description(APPLICATION_JSON),
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(
                        parameterWithName("productId").description("상품 ID"),
                        parameterWithName("optionId").description("상품 옵션 ID")
                ),
                requestFields(
                        fieldWithPath("name").description("옵션명 (최대 25자)"),
                        fieldWithPath("optionType").description("옵션 타입: `SELECT` | `INPUT`"),
                        fieldWithPath("usable").description("옵션 사용 가능 여부"),
                        fieldWithPath("optionValues")
                                .description("옵션 값 목록: `SELECT` - 최소 1개 이상 필수 | `INPUT` - 포함 불가")
                                .optional(),
                        fieldWithPath("optionValues[].name").description("옵션 값 (최대 30자)").optional(),
                        fieldWithPath("optionValues[].price").description("옵션 추가 금액 (기본값: 0)").optional(),
                        fieldWithPath("optionValues[].usable").description("옵션 값 사용 가능 여부").optional()
                ),
                responseHeaders(
                        headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                ),
                responseFields(
                        fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                        fieldWithPath("data.optionId").description("옵션 ID"),
                        fieldWithPath("data.productId").description("상품 ID"),
                        fieldWithPath("data.name").description("옵션명"),
                        fieldWithPath("data.optionType").description("옵션 타입"),
                        fieldWithPath("data.usable").description("옵션 사용 가능 여부"),
                        fieldWithPath("data.createdAt").description("옵션 생성 일시"),
                        fieldWithPath("data.updatedAt").description("옵션 수정 일시"),
                        fieldWithPath("data.optionValues").description("옵션 값 목록").optional(),
                        fieldWithPath("data.optionValues[].optionValueId").description("옵션 값 ID").optional(),
                        fieldWithPath("data.optionValues[].name").description("옵션 값 이름").optional(),
                        fieldWithPath("data.optionValues[].price").description("옵션 추가 금액").optional(),
                        fieldWithPath("data.optionValues[].usable").description("옵션 값 사용 가능 여부").optional(),
                        fieldWithPath("data.optionValues[].createdAt").description("옵션 값 생성 일시").optional(),
                        fieldWithPath("data.optionValues[].updatedAt").description("옵션 값 수정 일시").optional(),
                        fieldWithPath("message").description("처리 결과 메시지")
                )
        ));
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경: 옵션 값 추가")
    void shouldUpdateSelectWithMoreOptionValues() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption selectOption = createSelectProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "로스팅 정도",
                "SELECT",
                true,
                List.of(
                        new ProductOptionValueRequest("라이트 로스팅", 0, true),
                        new ProductOptionValueRequest("미디엄 로스팅", 2000, true),
                        new ProductOptionValueRequest("다크 로스팅", 4000, true),
                        new ProductOptionValueRequest("울트라 다크 로스팅", 5000, true) // 새로운 옵션 값 추가
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                selectOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(selectOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("로스팅 정도"))
                .andExpect(jsonPath("$.data.optionType").value("SELECT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues.length()").value(4))
                .andExpect(jsonPath("$.data.optionValues[0].name").value("라이트 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(0))
                .andExpect(jsonPath("$.data.optionValues[1].name").value("미디엄 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[1].price").value(2000))
                .andExpect(jsonPath("$.data.optionValues[2].name").value("다크 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[2].price").value(4000))
                .andExpect(jsonPath("$.data.optionValues[3].name").value("울트라 다크 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[3].price").value(5000));
    }

    @Test
    @DisplayName("✅ INPUT → SELECT 변경: 기존 옵션 값 없음, 새로운 옵션 값 추가")
    void shouldConvertInputToSelectWithNewOptionValues() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption inputOption = createInputProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "포장 방식",
                "SELECT",
                true,
                List.of(
                        new ProductOptionValueRequest("기본 포장", 0, true),
                        new ProductOptionValueRequest("선물 포장", 2000, true)
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                inputOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(inputOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("포장 방식"))
                .andExpect(jsonPath("$.data.optionType").value("SELECT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())

                // 옵션 값 개수 검증
                .andExpect(jsonPath("$.data.optionValues.length()").value(2))

                // 옵션 값 상세 검증
                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].name").value("기본 포장"))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(0))
                .andExpect(jsonPath("$.data.optionValues[0].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.optionValues[1].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].name").value("선물 포장"))
                .andExpect(jsonPath("$.data.optionValues[1].price").value(2000))
                .andExpect(jsonPath("$.data.optionValues[1].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("✅ SELECT → INPUT 변경: 기존 옵션 값 삭제")
    void shouldUpdateSelectToInput() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption selectOption = createSelectProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "배송 방식",
                "INPUT",
                true,
                List.of()
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                selectOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(selectOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("배송 방식"))
                .andExpect(jsonPath("$.data.optionType").value("INPUT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues").isEmpty());
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경: 옵션 값 개수 감소")
    void shouldDecreaseOptionValuesInSelectType() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption selectOption = createSelectProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "색상",
                "SELECT",
                true,
                List.of(
                        new ProductOptionValueRequest("레드", 1000, true)
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                selectOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(selectOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("색상"))
                .andExpect(jsonPath("$.data.optionType").value("SELECT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())

                // 옵션 값 개수 확인 (기존 2개에서 1개로 감소)
                .andExpect(jsonPath("$.data.optionValues.length()").value(1))

                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].name").value("레드"))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(1000))
                .andExpect(jsonPath("$.data.optionValues[0].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경: 옵션 값 개수 증가")
    void shouldIncreaseOptionValuesInSelectType() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption selectOption = createSelectProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "로스팅 정도",
                "SELECT",
                true,
                List.of(
                        new ProductOptionValueRequest("라이트 로스팅", 0, true),
                        new ProductOptionValueRequest("미디엄 로스팅", 2000, true),
                        new ProductOptionValueRequest("다크 로스팅", 4000, true),
                        new ProductOptionValueRequest("울트라 다크 로스팅", 6000, true)
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                selectOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(selectOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("로스팅 정도"))
                .andExpect(jsonPath("$.data.optionType").value("SELECT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())

                // 옵션 값 개수 확인 (기존 2개에서 4개로 증가)
                .andExpect(jsonPath("$.data.optionValues.length()").value(4))

                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].name").value("라이트 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(0))
                .andExpect(jsonPath("$.data.optionValues[0].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.optionValues[1].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].name").value("미디엄 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[1].price").value(2000))
                .andExpect(jsonPath("$.data.optionValues[1].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.optionValues[2].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[2].name").value("다크 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[2].price").value(4000))
                .andExpect(jsonPath("$.data.optionValues[2].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[2].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[2].updatedAt").isNotEmpty())
                // 추가된 옵션 값 확인
                .andExpect(jsonPath("$.data.optionValues[3].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[3].name").value("울트라 다크 로스팅"))
                .andExpect(jsonPath("$.data.optionValues[3].price").value(6000))
                .andExpect(jsonPath("$.data.optionValues[3].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[3].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[3].updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("✅ SELECT → SELECT 변경: 옵션 값 순서 변경")
    void shouldRearrangeOptionValuesInSelectType() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption selectOption = createSelectProductOption(product);

        ProductOptionUpdateRequest request = new ProductOptionUpdateRequest(
                "색상",
                "SELECT",
                true,
                List.of(
                        new ProductOptionValueRequest("블루", 1500, true), // 기존 순서: "레드" → "블루"
                        new ProductOptionValueRequest("레드", 1000, true)  // 변경 후: "블루" → "레드"
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}/options/{optionId}",
                product.id(),
                selectOption.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("상품 옵션이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.data.optionId").value(selectOption.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("색상"))
                .andExpect(jsonPath("$.data.optionType").value("SELECT"))
                .andExpect(jsonPath("$.data.usable").value(true))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())

                // 옵션 값 개수 확인 (기존 2개 유지)
                .andExpect(jsonPath("$.data.optionValues.length()").value(2))

                // 옵션 값 순서가 변경되었는지 확인
                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].name").value("블루"))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(1500))
                .andExpect(jsonPath("$.data.optionValues[0].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.optionValues[1].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].name").value("레드"))
                .andExpect(jsonPath("$.data.optionValues[1].price").value(1000))
                .andExpect(jsonPath("$.data.optionValues[1].usable").value(true))
                .andExpect(jsonPath("$.data.optionValues[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[1].updatedAt").isNotEmpty());
    }

}