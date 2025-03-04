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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class ProductOptionControllerOptionsApiTest extends IntegrationTestSupport {

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
                .name("로스팅 정도")
                .optionType(ProductOptionType.SELECT)
                .usable(true)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .optionValues(List.of(
                        new ProductOptionValue(ProductOptionValueId.withoutId(),
                                optionId,
                                "라이트 로스팅",
                                0,
                                true,
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now()),
                        new ProductOptionValue(ProductOptionValueId.withoutId(),
                                optionId,
                                "미디엄 로스팅",
                                2000,
                                true,
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now()),
                        new ProductOptionValue(ProductOptionValueId.withoutId(),
                                optionId,
                                "다크 로스팅",
                                4000,
                                true,
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now())
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
                .build();
        return productOptionRepository.saveWithOptionValues(option);
    }

    @Test
    @DisplayName("product options api")
    void docsProductOptionsApi() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption selectProductOption = createSelectProductOption(product);
        ProductOption inputProductOption = createInputProductOption(product);

        //  When
        ResultActions actions = mockMvc.perform(get("/products/{productId}/options", product.id())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.totalCount").value(2))
                .andExpect(jsonPath("$.data.options").isArray())
                .andExpect(jsonPath("$.data.options.length()").value(2))
                .andExpect(jsonPath("$.data.options[0].optionId").value(selectProductOption.id()))
                .andExpect(jsonPath("$.data.options[0].name").value(selectProductOption.name()))
                .andExpect(jsonPath("$.data.options[0].optionType").value(ProductOptionType.SELECT.name()))
                .andExpect(jsonPath("$.data.options[0].usable").value(selectProductOption.usable()))
                .andExpect(jsonPath("$.data.options[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[0].optionValues").isArray())
                .andExpect(jsonPath("$.data.options[0].optionValues.length()").value(selectProductOption.optionValues().size()))
                .andExpect(jsonPath("$.data.options[0].optionValues[0].optionValueId").value(selectProductOption.optionValues().getFirst().id()))
                .andExpect(jsonPath("$.data.options[0].optionValues[0].name").value(selectProductOption.optionValues().getFirst().name()))
                .andExpect(jsonPath("$.data.options[0].optionValues[0].price").value(selectProductOption.optionValues().getFirst().price()))
                .andExpect(jsonPath("$.data.options[0].optionValues[0].usable").value(selectProductOption.optionValues().getFirst().usable()))
                .andExpect(jsonPath("$.data.options[0].optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[0].optionValues[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[0].optionValues[2].optionValueId").value(selectProductOption.optionValues().getLast().id()))
                .andExpect(jsonPath("$.data.options[0].optionValues[2].name").value(selectProductOption.optionValues().getLast().name()))
                .andExpect(jsonPath("$.data.options[0].optionValues[2].price").value(selectProductOption.optionValues().getLast().price()))
                .andExpect(jsonPath("$.data.options[0].optionValues[2].usable").value(selectProductOption.optionValues().getLast().usable()))
                .andExpect(jsonPath("$.data.options[0].optionValues[2].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[0].optionValues[2].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$.data.options[1].optionId").value(inputProductOption.id()))
                .andExpect(jsonPath("$.data.options[1].name").value(inputProductOption.name()))
                .andExpect(jsonPath("$.data.options[1].optionType").value(ProductOptionType.INPUT.name()))
                .andExpect(jsonPath("$.data.options[1].usable").value(inputProductOption.usable()))
                .andExpect(jsonPath("$.data.options[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[1].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.options[1].optionValues").isEmpty())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                                headerWithName(ACCEPT).description(APPLICATION_JSON),
                                headerWithName(AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("productId").description("상품 ID")
                        ),
                        responseHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                                fieldWithPath("data.options").description("상품 옵션 목록"),
                                fieldWithPath("data.options[].productId").description("상품 ID"),
                                fieldWithPath("data.options[].optionId").description("옵션 ID"),
                                fieldWithPath("data.options[].name").description("옵션명"),
                                fieldWithPath("data.options[].optionType").description("옵션 타입: `INPUT` | `SELECT`"),
                                fieldWithPath("data.options[].usable").description("옵션 사용 가능 여부"),
                                fieldWithPath("data.options[].createdAt").description("옵션 생성 일시"),
                                fieldWithPath("data.options[].updatedAt").description("옵션 수정 일시"),
                                fieldWithPath("data.options[].optionValues").description("옵션 값 목록").optional(),
                                fieldWithPath("data.options[].optionValues[].optionValueId").description("옵션 값 ID").optional(),
                                fieldWithPath("data.options[].optionValues[].name").description("옵션 값 이름").optional(),
                                fieldWithPath("data.options[].optionValues[].price").description("옵션 추가 금액").optional(),
                                fieldWithPath("data.options[].optionValues[].usable").description("옵션 값 사용 가능 여부").optional(),
                                fieldWithPath("data.options[].optionValues[].createdAt").description("옵션 값 생성 일시").optional(),
                                fieldWithPath("data.options[].optionValues[].updatedAt").description("옵션 값 수정 일시").optional(),
                                fieldWithPath("data.totalCount").description("상품 옵션 개수"),
                                fieldWithPath("message").description("처리 결과 메시지")
                        )
                ));
    }
}
