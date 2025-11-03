package com.financial.apigateway.infrastructure.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private record ApiError(Instant timestamp, int status, String error, String message, String path, String code) {}

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = "INTERNAL_ERROR";
        String message = ex.getMessage();

        if (ex instanceof ResponseStatusException rse) {
            status = rse.getStatusCode() instanceof HttpStatus ? (HttpStatus) rse.getStatusCode() : HttpStatus.valueOf(rse.getStatusCode().value());
            message = rse.getReason();
            code = switch (status) {
                case BAD_REQUEST -> "BAD_REQUEST";
                case UNAUTHORIZED -> "UNAUTHORIZED";
                case FORBIDDEN -> "FORBIDDEN";
                case NOT_FOUND -> "NOT_FOUND";
                default -> code;
            };
        }

        ApiError apiError = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath().value(),
                code
        );

        byte[] bytes = ("{" +
                "\"timestamp\":\"" + apiError.timestamp() + "\"," +
                "\"status\":" + apiError.status() + "," +
                "\"error\":\"" + apiError.error() + "\"," +
                "\"message\":\"" + (apiError.message() == null ? "" : apiError.message().replace("\"", "'")) + "\"," +
                "\"path\":\"" + apiError.path() + "\"," +
                "\"code\":\"" + apiError.code() + "\"}" ).getBytes(StandardCharsets.UTF_8);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
