package com.financial.apigateway.application.service;

import com.financial.apigateway.domain.model.Route;
import com.financial.apigateway.domain.port.inbound.RouteManagementUseCase;
import com.financial.apigateway.domain.port.outbound.RouteRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RouteManagementService implements RouteManagementUseCase {

    private final RouteRepositoryPort routeRepositoryPort;

    public RouteManagementService(RouteRepositoryPort routeRepositoryPort) {
        this.routeRepositoryPort = routeRepositoryPort;
    }

    @Override
    public Flux<Route> getAllRoutes() {
        return routeRepositoryPort.findAllRoutes();
    }

    @Override
    public Mono<Route> getRouteById(String routeId) {
        if (routeId == null || routeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Route ID não pode ser nulo ou vazio"));
        }
        
        return routeRepositoryPort.findRouteById(routeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Rota não encontrada : " + routeId)));
    }

    @Override
    public Mono<Boolean> requiresAuthentication(String path) {
        if (path == null || path.trim().isEmpty()) {
            return Mono.just(false);
        }
        
        return routeRepositoryPort.findRouteByPath(path)
                .map(Route::isAuthRequired)
                .defaultIfEmpty(false);
    }
}
