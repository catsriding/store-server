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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
class ProductOptionControllerDeleteApiTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;

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

    private ProductOption createProductOption(Product product) {
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

    @Test
    @DisplayName("product option delete api")
    void docsProductOptionDeleteApi() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createProduct(user);
        ProductOption option = createProductOption(product);

        //  When
        ResultActions actions = mockMvc.perform(delete("/products/{productId}/options/{optionId}",
                product.id(),
                option.id())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.optionId").value(option.id()))
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.name").value(option.name()))
                .andExpect(jsonPath("$.data.updatedAt").isNotEmpty())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                                headerWithName(ACCEPT).description(APPLICATION_JSON),
                                headerWithName(AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("productId").description("상품 ID"),
                                parameterWithName("optionId").description("옵션 ID")
                        ),
                        responseHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                                fieldWithPath("data.optionId").description("삭제된 옵션 ID"),
                                fieldWithPath("data.productId").description("상품 ID"),
                                fieldWithPath("data.name").description("삭제된 옵션명"),
                                fieldWithPath("data.updatedAt").description("옵션 삭제 처리된 일시"),
                                fieldWithPath("message").description("처리 결과 메시지")
                        )
                ));

    }

}