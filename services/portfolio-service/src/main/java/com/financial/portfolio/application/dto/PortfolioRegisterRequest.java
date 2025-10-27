package com.financial.portfolio.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PortfolioRegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    private Long clientId;

}
