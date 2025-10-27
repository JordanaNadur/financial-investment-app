package com.financial.apigateway.infrastructure.adapter.outbound;

import com.financial.apigateway.domain.model.Route;
import com.financial.apigateway.domain.port.outbound.RouteRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class InMemoryRouteRepositoryAdapter implements RouteRepositoryPort {

    private final Map<String, Route> routes = new ConcurrentHashMap<>();

    public InMemoryRouteRepositoryAdapter() {
        initializeRoutes();
    }

    private void initializeRoutes() {
        Route authLoginRoute = new Route("auth-login", "/api/auth/login", "http://localhost:8081", "POST", false);
        Route authRegisterRoute = new Route("auth-register", "/api/auth/register", "http://localhost:8081", "POST", false);
        Route authValidateRoute = new Route("auth-validate", "/api/auth/validate", "http://localhost:8081", "POST", false);
        Route authAllRoute = new Route("auth-all", "/api/auth/*", "http://localhost:8081", "ALL", false);

        Route investmentListRoute = new Route("investment-list", "/api/investments", "http://localhost:8082", "GET", true);
        Route investmentCreateRoute = new Route("investment-create", "/api/investments", "http://localhost:8082", "POST", true);
        Route investmentByIdRoute = new Route("investment-by-id", "/api/investments/*", "http://localhost:8082", "GET", true);

        Route notificationSendRoute = new Route("notification-send", "/api/notifications", "http://localhost:8083", "POST", true);
        Route notificationListRoute = new Route("notification-list", "/api/notifications", "http://localhost:8083", "GET", true);

        routes.put(authLoginRoute.getId(), authLoginRoute);
        routes.put(authRegisterRoute.getId(), authRegisterRoute);
        routes.put(authValidateRoute.getId(), authValidateRoute);
        routes.put(authAllRoute.getId(), authAllRoute);
        routes.put(investmentListRoute.getId(), investmentListRoute);
        routes.put(investmentCreateRoute.getId(), investmentCreateRoute);
        routes.put(investmentByIdRoute.getId(), investmentByIdRoute);
        routes.put(notificationSendRoute.getId(), notificationSendRoute);
        routes.put(notificationListRoute.getId(), notificationListRoute);
    }

    @Override
    public Flux<Route> findAllRoutes() {
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Route> findRouteById(String routeId) {
        return Mono.justOrEmpty(routes.get(routeId));
    }

    @Override
    public Mono<Route> findRouteByPath(String path) {
        return Flux.fromIterable(routes.values())
                .filter(route -> pathMatches(route.getPath(), path))
                .sort((r1, r2) -> {
                    if (r1.getPath().contains("*") && !r2.getPath().contains("*")) {
                        return 1;
                    }
                    if (!r1.getPath().contains("*") && r2.getPath().contains("*")) {
                        return -1;
                    }
                    return r2.getPath().length() - r1.getPath().length();
                })
                .next();
    }

    private boolean pathMatches(String routePath, String requestPath) {
        if (routePath.equals(requestPath)) {
            return true;
        }
        if (routePath.endsWith("/*")) {
            String baseRoute = routePath.substring(0, routePath.length() - 2);
            return requestPath.startsWith(baseRoute);
        }
        return false;
    }
}
