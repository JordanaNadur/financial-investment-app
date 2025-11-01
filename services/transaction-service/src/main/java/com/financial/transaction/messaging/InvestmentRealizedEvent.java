package com.financial.transaction.messaging;

public class InvestmentRealizedEvent {
    private Long clientId;
    private Long transactionId;
    private String message;

    public InvestmentRealizedEvent() {}

    public InvestmentRealizedEvent(Long clientId, Long transactionId, String message) {
        this.clientId = clientId;
        this.transactionId = transactionId;
        this.message = message;
    }

    // Getters and Setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
