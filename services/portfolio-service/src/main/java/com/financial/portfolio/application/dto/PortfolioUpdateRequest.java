package com.financial.portfolio.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PortfolioUpdateRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

}

