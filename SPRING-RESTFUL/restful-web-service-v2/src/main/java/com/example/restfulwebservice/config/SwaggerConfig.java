package com.example.restfulwebservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class SwaggerConfig {

    private static final Contact DEFAULT_CONTACT =
            new Contact()
                    .name("Kenneth Lee")
                    .url("http://www.joneconsulting.co.kr")
                    .email("edowon@joneconsulting.co.kr");
    private static final Info DEFAULT_INFO =
            new Info()
                    .title("Awesome API Title")
                    .description("My user management REST API service")
                    .version("1.0")
                    .contact(DEFAULT_CONTACT)
                    .license(new License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0"));

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Arrays.asList("application/json", "application/xml"));

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(DEFAULT_INFO);
    }

}
