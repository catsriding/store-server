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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.catsriding.store.domain.user.User;
import com.catsriding.store.web.api.product.request.ProductRegistrationRequest;
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class ProductControllerRegistrationApiTest extends IntegrationTestSupport {

    private ProductRegistrationRequest createDefaultProduct() {
        return new ProductRegistrationRequest(
                "커피 블렌딩 원두",
                "고급 아라비카 원두입니다.",
                15000,
                2500,
                "SALE"
        );
    }

    @Test
    @DisplayName("product registration api")
    void docsProductRegistrationApi() throws Exception {

        //  Given
        User user = createTestUser();
        ProductRegistrationRequest request = createDefaultProduct();

        //  When
        ResultActions actions = mockMvc.perform(post("/products")
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
                                headerWithName(AUTHORIZATION).description("JSON Web Token")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("description").description("상품 설명"),
                                fieldWithPath("price").description("상품 가격: 최소 10원 이상"),
                                fieldWithPath("deliveryFee").description("배송비: 0원 이상"),
                                fieldWithPath("statusType").description("상품 상태 - `SALE` | `INACTIVE`")
                        ),
                        responseHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                                fieldWithPath("data.productId").description("상품 ID"),
                                fieldWithPath("data.sellerId").description("판매자 ID"),
                                fieldWithPath("data.name").description("상품명"),
                                fieldWithPath("data.status").description("상품 상태: `SALE` | `INACTIVE` | `DELETED`"),
                                fieldWithPath("data.createdAt").description("상품 등록 일시"),
                                fieldWithPath("message").description("처리 결과 메시지")
                        )
                ));
    }
}