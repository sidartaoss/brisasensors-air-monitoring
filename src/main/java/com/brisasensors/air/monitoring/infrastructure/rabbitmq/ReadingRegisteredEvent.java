package com.brisasensors.air.monitoring.infrastructure.rabbitmq;

import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingRegisteredEvent {
    private UUID id;
    private TSID deviceId;
    private OffsetDateTime measuredAt;
    private OffsetDateTime registeredAt;
    private Double co2Ppm;
}
