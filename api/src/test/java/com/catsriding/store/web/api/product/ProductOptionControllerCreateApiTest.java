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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.web.api.product.request.ProductOptionCreateRequest;
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class ProductOptionControllerCreateApiTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;

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

    @Test
    @DisplayName("product option create api")
    void docsProductOptionCreateApi() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);

        ProductOptionCreateRequest request = new ProductOptionCreateRequest(
                "로스팅 정도",
                "SELECT",
                true,
                List.of(
                        new ProductOptionCreateRequest.ProductOptionValueRequest("생두", 0, false),
                        new ProductOptionCreateRequest.ProductOptionValueRequest("라이트 로스팅", 2_500, true),
                        new ProductOptionCreateRequest.ProductOptionValueRequest("미디엄 로스팅", 5_000, true),
                        new ProductOptionCreateRequest.ProductOptionValueRequest("다크 로스팅", 10_000, true)
                )
        );

        //  When
        ResultActions actions = mockMvc.perform(post("/products/{productId}/options", product.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.optionId").isNotEmpty())
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value(request.name()))
                .andExpect(jsonPath("$.data.optionType").value(request.optionType()))
                .andExpect(jsonPath("$.data.usable").value(request.usable()))
                .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues").isArray())
                .andExpect(jsonPath("$.data.optionValues.length()").value(request.optionValues().size()))
                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].name").value(request.optionValues().getFirst().name()))
                .andExpect(jsonPath("$.data.optionValues[0].price").value(request.optionValues().getFirst().price()))
                .andExpect(jsonPath("$.data.optionValues[0].usable").value(request.optionValues().getFirst().usable()))
                .andExpect(jsonPath("$.data.optionValues[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[0].optionValueId").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[3].name").value(request.optionValues().getLast().name()))
                .andExpect(jsonPath("$.data.optionValues[3].price").value(request.optionValues().getLast().price()))
                .andExpect(jsonPath("$.data.optionValues[3].usable").value(request.optionValues().getLast().usable()))
                .andExpect(jsonPath("$.data.optionValues[3].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.data.optionValues[3].updatedAt").isNotEmpty())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                                headerWithName(ACCEPT).description(APPLICATION_JSON),
                                headerWithName(AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("productId").description("상품 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("옵션명 (최대 25자)"),
                                fieldWithPath("optionType").description("옵션 타입: `INPUT` | `SELECT`"),
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

}