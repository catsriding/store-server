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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class ProductControllerPagedApiTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    private Product createProduct(User user) {
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
    @DisplayName("product paged api")
    void docsProductPagedApi() throws Exception {

        //  Given
        User user = createTestUser();
        createProduct(user);
        int pageNumber = 0;
        int pageSize = 10;

        //  When
        ResultActions actions = mockMvc.perform(get("/products")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
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
                        queryParameters(
                                parameterWithName("pageNumber").description("조회할 페이지 번호: 시작 번호 0"),
                                parameterWithName("pageSize").description("한 페이지에 조회할 개수")
                        ),
                        responseHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                                fieldWithPath("data.content").description("상품 목록 데이터"),
                                fieldWithPath("data.content[].productId").description("상품 ID"),
                                fieldWithPath("data.content[].sellerId").description("판매자 ID"),
                                fieldWithPath("data.content[].name").description("상품명"),
                                fieldWithPath("data.content[].price").description("상품 가격"),
                                fieldWithPath("data.totalCount").description("총 상품 개수"),
                                fieldWithPath("data.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.pageNumber").description("현재 페이지 번호"),
                                fieldWithPath("data.pageSize").description("한 페이지당 상품 개수"),
                                fieldWithPath("data.isFirst").description("첫 페이지 여부"),
                                fieldWithPath("data.isLast").description("마지막 페이지 여부"),
                                fieldWithPath("data.hasNext").description("다음 페이지 존재 여부"),
                                fieldWithPath("message").description("처리 결과 메시지")
                        )
                ));
    }
}