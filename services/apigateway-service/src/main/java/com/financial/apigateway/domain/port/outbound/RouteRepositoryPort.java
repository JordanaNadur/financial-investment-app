package com.financial.apigateway.domain.port.outbound;

import com.financial.apigateway.domain.model.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteRepositoryPort {

    Flux<Route> findAllRoutes();

    Mono<Route> findRouteById(String routeId);

    Mono<Route> findRouteByPath(String path);
}
