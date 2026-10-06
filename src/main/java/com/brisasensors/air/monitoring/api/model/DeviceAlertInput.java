package com.brisasensors.air.monitoring.api.model;

import lombok.Data;

@Data
public class DeviceAlertInput {
    private Double maxCo2Ppm;
    private Integer sustainedMinutes;
}
