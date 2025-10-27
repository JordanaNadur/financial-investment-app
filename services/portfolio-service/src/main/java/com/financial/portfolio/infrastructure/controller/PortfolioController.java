package com.financial.portfolio.infrastructure.controller;

import com.financial.portfolio.application.dto.PortfolioResponse;
import com.financial.portfolio.application.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
@Tag(name = "Portfolio", description = "Portfolio endpoints")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    @Operation(summary = "Get portfolio")
    public ResponseEntity<PortfolioResponse> list(@RequestParam String name) throws Exception {
        return ResponseEntity.ok(portfolioService.getPortfolioByName(name));
    }

}
