package com.aihoo.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI aihooAdminOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aihoo Admin API")
                        .description("互联网医院管理后台 API（DDD 重构后）")
                        .version("1.0.0"));
    }
}
