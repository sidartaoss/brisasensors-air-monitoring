package com.brisasensors.air.monitoring.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceAlertOutput {
    private TSID id;
    private Double maxCo2Ppm;
    private Integer sustainedMinutes;
}
