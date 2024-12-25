package com.example.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GatewayFilter, Ordered {
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("Path requested: {}", exchange.getRequest().getPath());

    return chain.filter(exchange)
        .doOnSuccess(result -> {
          var response = exchange.getResponse();
          log.info("Response completed with status: {}", response.getStatusCode());
        });
  }

  @Override
  public int getOrder() {
    return 0;
  }
}