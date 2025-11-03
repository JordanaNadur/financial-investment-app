package com.financial.notification.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitConfig {

    // Exchange
    @Value("${rabbitmq.exchange.investment}")
    private String investmentExchange;

    // Queues
    @Value("${rabbitmq.queue.investment-created}")
    private String investmentCreatedQueue;

    @Value("${rabbitmq.queue.investment-withdrawn}")
    private String investmentWithdrawnQueue;

    @Value("${rabbitmq.queue.investment-notification-dlq}")
    private String investmentNotificationDlq;

    // Routing Keys
    @Value("${rabbitmq.routing-key.investment-created}")
    private String investmentCreatedRoutingKey;

    @Value("${rabbitmq.routing-key.investment-withdrawn}")
    private String investmentWithdrawnRoutingKey;

    @Value("${rabbitmq.routing-key.investment-notification-dlq}")
    private String investmentNotificationDlqRoutingKey;

    // DLQ Configuration
    @Bean
    public TopicExchange investmentExchange() {
        return new TopicExchange(investmentExchange, true, false);
    }

    // Queues principais
    @Bean
    public Queue investmentCreatedQueue() {
        return QueueBuilder.durable(investmentCreatedQueue)
                .withArgument("x-dead-letter-exchange", investmentExchange)
                .withArgument("x-dead-letter-routing-key", investmentNotificationDlqRoutingKey)
                .build();
    }

    @Bean
    public Queue investmentWithdrawnQueue() {
        return QueueBuilder.durable(investmentWithdrawnQueue)
                .withArgument("x-dead-letter-exchange", investmentExchange)
                .withArgument("x-dead-letter-routing-key", investmentNotificationDlqRoutingKey)
                .build();
    }

    // Dead Letter Queue
    @Bean
    public Queue investmentNotificationDlq() {
        return new Queue(investmentNotificationDlq, true);
    }

    // Bindings
    @Bean
    public Binding investmentCreatedBinding(Queue investmentCreatedQueue, TopicExchange investmentExchange) {
        return BindingBuilder.bind(investmentCreatedQueue)
                .to(investmentExchange)
                .with(investmentCreatedRoutingKey);
    }

    @Bean
    public Binding investmentWithdrawnBinding(Queue investmentWithdrawnQueue, TopicExchange investmentExchange) {
        return BindingBuilder.bind(investmentWithdrawnQueue)
                .to(investmentExchange)
                .with(investmentWithdrawnRoutingKey);
    }

    @Bean
    public Binding investmentNotificationDlqBinding(Queue investmentNotificationDlq, TopicExchange investmentExchange) {
        return BindingBuilder.bind(investmentNotificationDlq)
                .to(investmentExchange)
                .with(investmentNotificationDlqRoutingKey);
    }

    // Configuração de Retry
    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000) // initialInterval, multiplier, maxInterval
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RetryOperationsInterceptor retryInterceptor,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAdviceChain(retryInterceptor);
        factory.setPrefetchCount(1);
        // Converter JSON -> POJO nas @RabbitListener methods
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        // Prioriza o tipo inferido pelo parâmetro do método (@RabbitListener) ao invés do __TypeId__ do header
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setAlwaysConvertToInferredType(true);
        // Confiar em todos os pacotes e usar mapper dedicado
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        javaTypeMapper.setTrustedPackages("*");
        javaTypeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(javaTypeMapper);
        return converter;
    }

    // RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // Usa o MessageConverter do contexto (com ObjectMapper configurado)
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }
}