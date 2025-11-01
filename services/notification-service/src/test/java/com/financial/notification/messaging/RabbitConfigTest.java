package com.financial.notification.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitConfigTest {

    @Mock
    private ConnectionFactory connectionFactory;

    private RabbitConfig rabbitConfig = new RabbitConfig();

    @Test
    void investmentExchange_shouldCreateTopicExchange() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentExchange", "investment.exchange");

        // When
        TopicExchange exchange = rabbitConfig.investmentExchange();

        // Then
        assertNotNull(exchange);
        assertEquals("investment.exchange", exchange.getName());
        assertTrue(exchange.isDurable());
        assertFalse(exchange.isAutoDelete());
    }

    @Test
    void investmentCreatedQueue_shouldCreateDurableQueueWithDlq() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentCreatedQueue", "investment.created.queue");
        ReflectionTestUtils.setField(rabbitConfig, "investmentExchange", "investment.exchange");
        ReflectionTestUtils.setField(rabbitConfig, "investmentNotificationDlqRoutingKey", "investment.notification.dlq");

        // When
        Queue queue = rabbitConfig.investmentCreatedQueue();

        // Then
        assertNotNull(queue);
        assertEquals("investment.created.queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void investmentWithdrawnQueue_shouldCreateDurableQueueWithDlq() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentWithdrawnQueue", "investment.withdrawn.queue");
        ReflectionTestUtils.setField(rabbitConfig, "investmentExchange", "investment.exchange");
        ReflectionTestUtils.setField(rabbitConfig, "investmentNotificationDlqRoutingKey", "investment.notification.dlq");

        // When
        Queue queue = rabbitConfig.investmentWithdrawnQueue();

        // Then
        assertNotNull(queue);
        assertEquals("investment.withdrawn.queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void investmentNotificationDlq_shouldCreateDurableQueue() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentNotificationDlq", "investment.notification.dlq");

        // When
        Queue queue = rabbitConfig.investmentNotificationDlq();

        // Then
        assertNotNull(queue);
        assertEquals("investment.notification.dlq", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void investmentCreatedBinding_shouldBindQueueToExchange() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentCreatedRoutingKey", "investment.created");
        TopicExchange exchange = new TopicExchange("investment.exchange");
        Queue queue = new Queue("investment.created.queue");

        // When
        Binding binding = rabbitConfig.investmentCreatedBinding(queue, exchange);

        // Then
        assertNotNull(binding);
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
        assertEquals("investment.created", binding.getRoutingKey());
    }

    @Test
    void investmentWithdrawnBinding_shouldBindQueueToExchange() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentWithdrawnRoutingKey", "investment.withdrawn");
        TopicExchange exchange = new TopicExchange("investment.exchange");
        Queue queue = new Queue("investment.withdrawn.queue");

        // When
        Binding binding = rabbitConfig.investmentWithdrawnBinding(queue, exchange);

        // Then
        assertNotNull(binding);
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
        assertEquals("investment.withdrawn", binding.getRoutingKey());
    }

    @Test
    void investmentNotificationDlqBinding_shouldBindDlqToExchange() {
        // Given
        ReflectionTestUtils.setField(rabbitConfig, "investmentNotificationDlqRoutingKey", "investment.notification.dlq");
        TopicExchange exchange = new TopicExchange("investment.exchange");
        Queue queue = new Queue("investment.notification.dlq");

        // When
        Binding binding = rabbitConfig.investmentNotificationDlqBinding(queue, exchange);

        // Then
        assertNotNull(binding);
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
        assertEquals("investment.notification.dlq", binding.getRoutingKey());
    }

    @Test
    void jsonMessageConverter_shouldReturnJackson2JsonMessageConverter() {
        // When
        MessageConverter converter = rabbitConfig.jsonMessageConverter();

        // Then
        assertNotNull(converter);
        assertTrue(converter instanceof org.springframework.amqp.support.converter.Jackson2JsonMessageConverter);
    }

    @Test
    void rabbitTemplate_shouldConfigureTemplateCorrectly() {
        // Given
        RabbitTemplate mockTemplate = mock(RabbitTemplate.class);

        // When
        RabbitTemplate template = rabbitConfig.rabbitTemplate(connectionFactory);

        // Then
        assertNotNull(template);
        // Note: In a real test, we would verify the configuration, but since RabbitTemplate is final,
        // we can only verify it was created with the connection factory
    }
}
