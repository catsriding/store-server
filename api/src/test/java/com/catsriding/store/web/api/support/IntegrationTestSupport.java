package com.catsriding.store.web.api.support;

import static com.catsriding.store.domain.user.UserRoleType.ROLE_USER;
import static com.catsriding.store.domain.user.UserStatusType.ACTIVE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.catsriding.store.domain.auth.TokenClaims;
import com.catsriding.store.domain.auth.TokenProvider;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.domain.user.UserId;
import com.catsriding.store.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected TokenProvider tokenProvider;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected ClockHolder clockHolder;
    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    protected String serialize(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected String userAccessToken(User user) {
        TokenClaims claims = TokenClaims.from(user.id(), user.username(), user.role().name());
        return tokenProvider.issue(claims, clockHolder.currentTimeMillis()).accessToken();
    }

    public User createTestUser() {
        User user = User.builder()
                .id(UserId.withoutId())
                .username("user@test.com")
                .password(passwordEncoder.encode("test1234"))
                .role(ROLE_USER)
                .status(ACTIVE)
                .createdAt(clockHolder.now())
                .updatedAt(clockHolder.now())
                .build();
        return userRepository.save(user);
    }

}
