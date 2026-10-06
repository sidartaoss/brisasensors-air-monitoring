package com.brisasensors.air.monitoring.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class DeviceMonitoringOutput {
    private TSID id;
    private Double lastCo2Ppm;
    private OffsetDateTime lastMeasuredAt;
    private Boolean enabled;
    private Boolean alerting;
}
