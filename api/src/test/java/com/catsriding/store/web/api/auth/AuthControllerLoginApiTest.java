package com.catsriding.store.web.api.auth;

import static java.util.Locale.KOREA;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.catsriding.store.domain.user.User;
import com.catsriding.store.web.api.auth.request.LoginRequest;
import com.catsriding.store.web.api.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@Tag("restdocs")
class AuthControllerLoginApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("user login api")
    void docsUserLoginApi() throws Exception {

        //  Given
        User user = createTestUser();
        LoginRequest request = new LoginRequest(user.username(), "test1234");

        //  When
        ResultActions actions = mockMvc.perform(post("/login")
                .content(serialize(request))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(ACCEPT_LANGUAGE, KOREA));

        //  Then
        actions
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON),
                                headerWithName(ACCEPT).description(APPLICATION_JSON)
                        ),
                        requestFields(
                                fieldWithPath("username").description("사용자 이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON)
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태: `SUCCESS` | `FAILURE`"),
                                fieldWithPath("data.tokenType").description("토큰 타입"),
                                fieldWithPath("data.accessToken").description("발급된 액세스 토큰"),
                                fieldWithPath("data.expiresIn").description("토큰 만료 시간 (단위: ms)"),
                                fieldWithPath("message").description("처리 결과 메시지")
                        )
                ));

    }

}