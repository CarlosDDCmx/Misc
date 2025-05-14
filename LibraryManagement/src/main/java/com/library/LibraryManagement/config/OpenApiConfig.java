package com.library.LibraryManagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Library Management API",
        version = "1.0.0",
        description = "API documentation for Library Management System",
        contact = @Contact(
            name = "API Support",
            email = "carlosd.dc.mxt@gmail.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .addServersItem(new Server().url("http://localhost:8081"))
            .addServersItem(new Server().url("https://api.library.com"));
    }
}