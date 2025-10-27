package com.financial.apigateway.domain.port.inbound;

import com.financial.apigateway.domain.model.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteManagementUseCase {

    Flux<Route> getAllRoutes();

    Mono<Route> getRouteById(String routeId);

    Mono<Boolean> requiresAuthentication(String path);
}
