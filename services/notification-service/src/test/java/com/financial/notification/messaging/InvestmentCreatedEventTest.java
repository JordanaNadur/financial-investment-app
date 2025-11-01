package com.financial.notification.messaging;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentCreatedEventTest {

    @Test
    void constructorAndGetters_shouldWorkCorrectly() {
        // Given
        Long investmentId = 1L;
        Long userId = 2L;
        String modality = "CDB";
        Double amount = 1000.0;
        Integer termInMonths = 12;
        BigDecimal monthlyReturn = BigDecimal.valueOf(50.0);

        // When
        InvestmentCreatedEvent event = new InvestmentCreatedEvent(investmentId, userId, modality, amount, termInMonths, monthlyReturn);

        // Then
        assertEquals(investmentId, event.getInvestmentId());
        assertEquals(userId, event.getUserId());
        assertEquals(modality, event.getModality());
        assertEquals(amount, event.getAmount());
        assertEquals(termInMonths, event.getTermInMonths());
        assertEquals(monthlyReturn, event.getMonthlyReturn());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        // When
        InvestmentCreatedEvent event = new InvestmentCreatedEvent();

        // Then
        assertNull(event.getInvestmentId());
        assertNull(event.getUserId());
        assertNull(event.getModality());
        assertNull(event.getAmount());
        assertNull(event.getTermInMonths());
        assertNull(event.getMonthlyReturn());
    }

    @Test
    void setters_shouldUpdateValues() {
        // Given
        InvestmentCreatedEvent event = new InvestmentCreatedEvent();

        // When
        event.setInvestmentId(1L);
        event.setUserId(2L);
        event.setModality("LCI");
        event.setAmount(2000.0);
        event.setTermInMonths(24);
        event.setMonthlyReturn(BigDecimal.valueOf(100.0));

        // Then
        assertEquals(1L, event.getInvestmentId());
        assertEquals(2L, event.getUserId());
        assertEquals("LCI", event.getModality());
        assertEquals(2000.0, event.getAmount());
        assertEquals(24, event.getTermInMonths());
        assertEquals(BigDecimal.valueOf(100.0), event.getMonthlyReturn());
    }

    @Test
    void equalsAndHashCode_shouldWorkForSameValues() {
        // Given
        InvestmentCreatedEvent event1 = new InvestmentCreatedEvent(1L, 1L, "CDB", 1000.0, 12, BigDecimal.valueOf(50.0));
        InvestmentCreatedEvent event2 = new InvestmentCreatedEvent(1L, 1L, "CDB", 1000.0, 12, BigDecimal.valueOf(50.0));

        // Then
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForDifferentValues() {
        // Given
        InvestmentCreatedEvent event1 = new InvestmentCreatedEvent(1L, 1L, "CDB", 1000.0, 12, BigDecimal.valueOf(50.0));
        InvestmentCreatedEvent event2 = new InvestmentCreatedEvent(2L, 1L, "CDB", 1000.0, 12, BigDecimal.valueOf(50.0));

        // Then
        assertNotEquals(event1, event2);
    }
}
