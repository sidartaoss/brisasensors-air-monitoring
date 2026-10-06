package com.brisasensors.air.monitoring.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    public static final String READING_REGISTERED_EXCHANGE = "ingestion.reading-registered.v1.e";
    private static final String PROCESS_READING = "air-monitoring.process-reading.v1";
    public static final String PROCESS_READING_QUEUE = PROCESS_READING + ".q";
    public static final String PROCESS_READING_DLQ = PROCESS_READING + ".dlq";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(final JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    // Single Active Consumer só pode ser ativado na declaração; o dead lettering vem da política do broker
    @Bean
    public Queue processReadingQueue() {
        return QueueBuilder.durable(PROCESS_READING_QUEUE)
                .singleActiveConsumer()
                .build();
    }

    @Bean
    public Queue processReadingDlq() {
        return QueueBuilder.durable(PROCESS_READING_DLQ).build();
    }

    // A exchange é da ingestão; declará-la aqui, com os mesmos atributos, evita depender da ordem de subida
    @Bean
    public FanoutExchange readingRegisteredExchange() {
        return ExchangeBuilder.fanoutExchange(READING_REGISTERED_EXCHANGE).build();
    }

    @Bean
    public Binding processReadingBinding() {
        return BindingBuilder.bind(processReadingQueue()).to(readingRegisteredExchange());
    }
}
