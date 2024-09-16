package com.uol.email_management_api.infrastructure.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String PROJECT_TITLE = "Email Management API";
    private static final String PROJECT_VERSION = "1.0";
    private static final String PROJECT_DESCRIPTION = "API for managing mailboxes and email-related operations";
    private static final String PROJECT_GROUP_V1 = "MailBox V1";
    private static final String PROJECT_GROUP_V2 = "MailBox V2";
    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "http://springdoc.org";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(PROJECT_TITLE)
                        .version(PROJECT_VERSION)
                        .description(PROJECT_DESCRIPTION)
                        .license(new License().name(LICENSE_NAME).url(LICENSE_URL)));
    }

    @Bean
    public GroupedOpenApi publicApiV1() {
        return GroupedOpenApi.builder()
                .group(PROJECT_GROUP_V1)
                .displayName(PROJECT_TITLE + " V1")
                .pathsToMatch("/api/v1/mailboxes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApiV2() {
        return GroupedOpenApi.builder()
                .group(PROJECT_GROUP_V2)
                .displayName(PROJECT_TITLE + " V2")
                .pathsToMatch("/api/v2/mailboxes/**")
                .build();
    }
}
