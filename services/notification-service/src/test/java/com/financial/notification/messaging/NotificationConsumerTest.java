package com.financial.notification.messaging;

import com.financial.notification.domain.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    private InvestmentCreatedEvent investmentCreatedEvent;
    private InvestmentWithdrawnEvent investmentWithdrawnEvent;

    @BeforeEach
    void setUp() {
        investmentCreatedEvent = new InvestmentCreatedEvent(1L, 1L, "CDB", 1000.0, 12, null);
        investmentWithdrawnEvent = new InvestmentWithdrawnEvent(1L, 1L, 500.0, null);
    }

    @Test
    void handleInvestmentCreated_shouldProcessSuccessfully() {
        // When
        notificationConsumer.handleInvestmentCreated(investmentCreatedEvent);

        // Then
        verify(notificationService, times(1)).processInvestmentCreatedNotification(investmentCreatedEvent);
    }

    @Test
    void handleInvestmentCreated_shouldLogAndRethrowException() {
        // Given
        doThrow(new RuntimeException("Processing error")).when(notificationService).processInvestmentCreatedNotification(investmentCreatedEvent);

        // When & Then
        try {
            notificationConsumer.handleInvestmentCreated(investmentCreatedEvent);
        } catch (RuntimeException e) {
            // Expected to rethrow
        }

        verify(notificationService, times(1)).processInvestmentCreatedNotification(investmentCreatedEvent);
    }

    @Test
    void handleInvestmentWithdrawn_shouldProcessSuccessfully() {
        // When
        notificationConsumer.handleInvestmentWithdrawn(investmentWithdrawnEvent);

        // Then
        verify(notificationService, times(1)).processInvestmentWithdrawnNotification(investmentWithdrawnEvent);
    }

    @Test
    void handleInvestmentWithdrawn_shouldLogAndRethrowException() {
        // Given
        doThrow(new RuntimeException("Processing error")).when(notificationService).processInvestmentWithdrawnNotification(investmentWithdrawnEvent);

        // When & Then
        try {
            notificationConsumer.handleInvestmentWithdrawn(investmentWithdrawnEvent);
        } catch (RuntimeException e) {
            // Expected to rethrow
        }

        verify(notificationService, times(1)).processInvestmentWithdrawnNotification(investmentWithdrawnEvent);
    }
}
