package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class FilterConfig {

    // 이제 안 쓸꺼야,,, application.yml 에 필터 추가
    // @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/first-service/**") // xxx 이 호출되면
                             .filters(f -> f.addRequestHeader("first-request", "first-request-header")  // 필터를 거쳐
                                            .addResponseHeader("first-response", "first-response"))
                             .uri("http://localhost:8081/"))    // ~~~ 로 전송
                .route(r -> r.path("/second-service/**") // xxx 이 호출되면
                        .filters(f -> f.addRequestHeader("second-request", "second-request-header")  // 필터를 거쳐
                                .addResponseHeader("second-response", "second-response"))
                        .uri("http://localhost:8082/"))    // ~~~ 로 전송
                .build();
    }

}
