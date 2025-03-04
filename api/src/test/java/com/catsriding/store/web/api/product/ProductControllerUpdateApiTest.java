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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.web.api.product.request.ProductUpdateRequest;
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class ProductControllerUpdateApiTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    private Product createExistingProduct(User user) {
        Product product = Product.builder()
                .id(ProductId.withId(1L))
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
    @DisplayName("product update api")
    void docsProductUpdateApi() throws Exception {

        //  Given
        User user = createTestUser();
        Product product = createExistingProduct(user);

        ProductUpdateRequest request = new ProductUpdateRequest(
                "프리미엄 원두",
                "더 고급스러운 원두",
                18000,
                3000,
                "INACTIVE"
        );

        //  When
        ResultActions actions = mockMvc.perform(put("/products/{productId}", product.id())
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA)
                .header(AUTHORIZATION, userAccessToken(user)));

        //  Then
        actions
                .andExpect(status().isOk())
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
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("description").description("상품 설명"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("deliveryFee").description("배송비"),
                                fieldWithPath("statusType").description("상품 상태: `SALE` | `INACTIVE`")
                        ),
                        responseHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                                fieldWithPath("data.productId").description("상품 ID"),
                                fieldWithPath("data.sellerId").description("판매자 ID"),
                                fieldWithPath("data.name").description("상품명"),
                                fieldWithPath("data.statusType").description("상품 상태: `SALE` | `INACTIVE` | `DELETED`"),
                                fieldWithPath("data.updatedAt").description("상품 수정 일시"),
                                fieldWithPath("data.createdAt").description("상품 등록 일시"),
                                fieldWithPath("message").description("처리 결과 메시지")
                        )
                ));
    }

}