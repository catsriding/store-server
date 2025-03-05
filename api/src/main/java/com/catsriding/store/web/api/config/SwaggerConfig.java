package com.catsriding.store.web.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI storeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Store API")
                        .summary("Test API Requests Directly ⚡")
                        .description("""
                                      Welcome to the Store API documentation.
                                     
                                     - 📝 [Spring REST DOCS](https://store-api.catsriding.com/docs/index.html)
                                     - 🐙 [GitHub Repository](https://github.com/catsriding/store-server)
                                     """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("👩🏻‍💻 youngsoojin")
                                .url("https://catsriding.com")
                                .email("catsriding@gmail.com")))

                .servers(List.of(
                        new Server().url("https://store-api.catsriding.com").description("💎 PRODUCTION"),
                        new Server().url("http://localhost:8080").description("💻 LOCAL DEVELOPMENT")))

                .addSecurityItem(new SecurityRequirement().addList("JSON Web Token"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("JSON Web Token", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                             🔑 로그인 후 발급받은 JWT 액세스 토큰을 입력하세요. `Bearer `(공백 포함 7자)는 자동으로 추가되므로, 액세스 토큰에서 이를 제거하고 입력해주세요. (예: eyJhbGciOi...)
                                             """)
                        ));
    }
}
