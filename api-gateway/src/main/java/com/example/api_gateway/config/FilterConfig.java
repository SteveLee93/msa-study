package com.example.api_gateway.config;

import com.example.api_gateway.filter.GlobalFilter;
import com.example.api_gateway.filter.LoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder,
      GlobalFilter globalFilter,
      LoggingFilter loggingFilter) {
    return builder.routes()
        .route("auth-service", r -> r
            .path("/api/auth/**")
            .filters(f -> f
                .filter(globalFilter)
                .filter(loggingFilter))
            .uri("http://auth-service:8081"))
        .route("user-service", r -> r
            .path("/api/users/**")
            .filters(f -> f
                .filter(globalFilter)
                .filter(loggingFilter))
            .uri("http://user-service:8082"))
        .build();
  }
}