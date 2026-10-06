package com.brisasensors.air.monitoring.infrastructure.rabbitmq;

import com.brisasensors.air.monitoring.domain.model.DeviceId;
import com.brisasensors.air.monitoring.domain.model.Reading;
import com.brisasensors.air.monitoring.domain.model.ReadingId;
import com.brisasensors.air.monitoring.domain.service.ReadingMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.brisasensors.air.monitoring.infrastructure.rabbitmq.RabbitMQConfig.PROCESS_READING_QUEUE;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final ReadingMonitoringService readingMonitoringService;

    // Um único consumidor ativo preserva a ordem das leituras de cada dispositivo
    @RabbitListener(queues = PROCESS_READING_QUEUE, concurrency = "1")
    public void handleReading(@Payload final ReadingRegisteredEvent event) {
        Reading reading = Reading.builder()
                .id(new ReadingId(event.getId()))
                .deviceId(new DeviceId(event.getDeviceId()))
                .co2Ppm(event.getCo2Ppm())
                .measuredAt(event.getMeasuredAt())
                .registeredAt(event.getRegisteredAt())
                .build();

        readingMonitoringService.processReading(reading);
    }
}
