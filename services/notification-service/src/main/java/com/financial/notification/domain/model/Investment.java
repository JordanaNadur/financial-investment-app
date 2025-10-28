package com.financial.notification.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double amount;

    // Novos campos para substituir a funcionalidade do Notification
    private Long userId;
    private Long investmentId;

    @Enumerated(EnumType.STRING)
    private InvestmentType type;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private String metadata;

    public enum InvestmentType {
        INVESTMENT_CREATED,
        INVESTMENT_WITHDRAWN
    }

    public enum InvestmentStatus {
        PENDING,
        SENT,
        FAILED
    }
}