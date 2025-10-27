package com.financial.apigateway.infrastructure.adapter.inbound.web;

import com.financial.apigateway.domain.model.Route;
import com.financial.apigateway.domain.port.inbound.RouteManagementUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/gateway")
public class RouteController {

    private final RouteManagementUseCase routeManagementUseCase;

    public RouteController(RouteManagementUseCase routeManagementUseCase) {
        this.routeManagementUseCase = routeManagementUseCase;
    }

    @GetMapping("/routes")
    public Flux<Route> getAllRoutes() {
        return routeManagementUseCase.getAllRoutes();
    }

    @GetMapping("/routes/{routeId}")
    public Mono<ResponseEntity<Route>> getRouteById(@PathVariable String routeId) {
        return routeManagementUseCase.getRouteById(routeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("API Gateway is running"));
    }
}
