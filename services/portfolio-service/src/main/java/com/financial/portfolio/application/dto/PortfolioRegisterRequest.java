package com.financial.portfolio.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PortfolioRegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clientId;

}
