package com.financial.catalog.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RunAfterInit {

    private static final Logger logger = LoggerFactory.getLogger(RunAfterInit.class);

    @Value("${server.port:8080}")
    private String serverPort;

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        logger.info("Swagger UI: http://localhost:{}/swagger-ui.html", serverPort);
    }
}
