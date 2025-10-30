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

    @Column(name = "user_id", nullable = false)
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
        private Double amount;
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
}