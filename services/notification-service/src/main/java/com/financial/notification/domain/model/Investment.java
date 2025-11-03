package com.financial.notification.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investment_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "valor")
    private BigDecimal amount;

    @Column(name = "monthly_return")
    private BigDecimal monthlyReturn;

    private Long investmentId;

    @Enumerated(EnumType.STRING)
    private InvestmentType type;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "data_investimento")
    private LocalDateTime dataInvestimento; // NOVO CAMPO ADICIONADO

    @Embedded
    private InvestmentDetails investmentDetails;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InvestmentDetails {
        private String investmentName;
        private String modality;
        private Double withdrawnAmount;
        private Integer termInMonths;
    }

    public enum InvestmentType {
        INVESTMENT_CREATED,
        INVESTMENT_WITHDRAWN
    }

    public enum InvestmentStatus {
        PENDING,
        SENT,
        FAILED
    }

    public enum TipoModalidade {
        RENDA_FIXA,
        RENDA_VARIAVEL,
        FUNDO_INVESTIMENTO,
        TESOURO_DIRETO,
        POUPANCA
        // Adicione outros tipos de investimento conforme necessário
    }
}
