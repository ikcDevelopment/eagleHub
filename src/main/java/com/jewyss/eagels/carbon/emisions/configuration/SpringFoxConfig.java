package com.jewyss.eagels.carbon.emisions.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @project Eagle hub
 * @coder estuardo.wyss
 * @date
 */
@Configuration
public class SpringFoxConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("eagles-admin-public")
                .pathsToMatch("/v1/emissions/admin/**")
                .build();
    }
}
