package com.financial.notification.messaging;

public class InvestmentCreatedEvent {

    private Long userId;
    private Long investmentId;
    private String message;

    public InvestmentCreatedEvent() {}

    public InvestmentCreatedEvent(Long userId, Long investmentId, String message) {
        this.userId = userId;
        this.investmentId = investmentId;
        this.message = message;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getInvestmentId() { return investmentId; }
    public void setInvestmentId(Long investmentId) { this.investmentId = investmentId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
