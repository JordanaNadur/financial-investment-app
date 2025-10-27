package com.financial.apigateway.infrastructure.filter;

import com.financial.apigateway.domain.port.inbound.JwtValidationUseCase;
import com.financial.apigateway.domain.port.inbound.RouteManagementUseCase;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    private final JwtValidationUseCase jwtValidationUseCase;
    private final RouteManagementUseCase routeManagementUseCase;

    public JwtAuthenticationGatewayFilterFactory(JwtValidationUseCase jwtValidationUseCase, 
                                                RouteManagementUseCase routeManagementUseCase) {
        super(Config.class);
        this.jwtValidationUseCase = jwtValidationUseCase;
        this.routeManagementUseCase = routeManagementUseCase;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            return routeManagementUseCase.requiresAuthentication(path)
                    .flatMap(requiresAuth -> {
                        if (!requiresAuth) {
                            return chain.filter(exchange);
                        }
                        
                        return validateToken(exchange)
                                .flatMap(isValid -> {
                                    if (isValid) {
                                        return chain.filter(exchange);
                                    } else {
                                        return unauthorizedResponse(exchange);
                                    }
                                });
                    });
        };
    }

    private Mono<Boolean> validateToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just(false);
        }

        String token = authHeader.substring(7);
        return jwtValidationUseCase.validateToken(token)
                .map(jwtToken -> !jwtToken.isExpired())
                .onErrorReturn(false);
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
    }
}
