package com.financial.notification.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "investment")
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID é obrigatório")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Investment ID é obrigatório")
    @Column(name = "investment_id", nullable = false)
    private Long investmentId;

    @NotNull(message = "Tipo da notificação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private InvestmentType type;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 1000, message = "Mensagem deve ter no máximo 1000 caracteres")
    @Column(name = "message", length = 1000)
    private String message;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvestmentStatus status;

    @NotNull(message = "Data de criação é obrigatória")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Size(max = 2000, message = "Metadados deve ter no máximo 2000 caracteres")
    @Column(name = "metadata", length = 2000)
    private String metadata;

    @Size(max = 255, message = "Email do destinatário deve ter no máximo 255 caracteres")
    @Column(name = "recipient_email", length = 255)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "investment_channel", length = 20)
    private InvestmentChannel channel;

    // Enums
    public enum InvestmentType {
        INVESTMENT_CREATED,
        INVESTMENT_WITHDRAWN,
        INVESTMENT_MATURED,
        PORTFOLIO_UPDATE,
        SYSTEM_ALERT
    }

    public enum InvestmentStatus {
        PENDING,
        SENT,
        DELIVERED,
        READ,
        FAILED
    }

    public enum InvestmentChannel {
        EMAIL,
        SMS,
        PUSH,
        IN_APP
    }

    // Constructors
    public Investment() {
        this.createdAt = LocalDateTime.now();
        this.status = InvestmentStatus.PENDING;
    }

    public Investment(Long userId, Long investmentId, InvestmentType type,
                        String title, String message) {
        this();
        this.userId = userId;
        this.investmentId = investmentId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.channel = InvestmentChannel.EMAIL; // Default channel
    }

    public Investment(Long userId, Long investmentId, InvestmentType type,
                        String title, String message, String recipientEmail,
                        InvestmentChannel channel) {
        this(userId, investmentId, type, title, message);
        this.recipientEmail = recipientEmail;
        this.channel = channel;
    }

    // Business methods
    public void markAsSent() {
        this.status = InvestmentStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        this.status = InvestmentStatus.DELIVERED;
    }

    public void markAsRead() {
        this.status = InvestmentStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = InvestmentStatus.FAILED;
    }

    public boolean isPending() {
        return InvestmentStatus.PENDING.equals(this.status);
    }

    public boolean isSent() {
        return InvestmentStatus.SENT.equals(this.status);
    }

    public boolean isRead() {
        return InvestmentStatus.READ.equals(this.status);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getInvestmentId() { return investmentId; }
    public void setInvestmentId(Long investmentId) { this.investmentId = investmentId; }

    public InvestmentType getType() { return type; }
    public void setType(InvestmentType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public InvestmentStatus getStatus() { return status; }
    public void setStatus(InvestmentStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public InvestmentChannel getChannel() { return channel; }
    public void setChannel(InvestmentChannel channel) { this.channel = channel; }

    // toString, equals, hashCode
    @Override
    public String toString() {
        return "Investment{" +
                "id=" + id +
                ", userId=" + userId +
                ", investmentId=" + investmentId +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    // Builder pattern para criação fluente
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long userId;
        private Long investmentId;
        private InvestmentType type;
        private String title;
        private String message;
        private String recipientEmail;
        private InvestmentChannel channel;
        private String metadata;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder investmentId(Long investmentId) {
            this.investmentId = investmentId;
            return this;
        }

        public Builder type(InvestmentType type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder recipientEmail(String recipientEmail) {
            this.recipientEmail = recipientEmail;
            return this;
        }

        public Builder channel(InvestmentChannel channel) {
            this.channel = channel;
            return this;
        }

        public Builder metadata(String metadata) {
            this.metadata = metadata;
            return this;
        }

        public Investment build() {
            Investment investment = new Investment();
            investment.setUserId(userId);
            investment.setInvestmentId(investmentId);
            investment.setType(type);
            investment.setTitle(title);
            investment.setMessage(message);
            investment.setRecipientEmail(recipientEmail);
            investment.setChannel(channel != null ? channel : InvestmentChannel.EMAIL);
            investment.setMetadata(metadata);
            return investment;
        }
    }
}