package com.brisasensors.air.monitoring.api.controller;

import com.brisasensors.air.monitoring.api.model.DeviceMonitoringOutput;
import com.brisasensors.air.monitoring.domain.model.DeviceId;
import com.brisasensors.air.monitoring.domain.model.DeviceMonitoring;
import com.brisasensors.air.monitoring.domain.repository.DeviceMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@RestController
@RequestMapping("/api/devices/{deviceId}/monitoring")
@RequiredArgsConstructor
public class DeviceMonitoringController {

    private final DeviceMonitoringRepository deviceMonitoringRepository;

    @GetMapping
    public DeviceMonitoringOutput getDetail(@PathVariable TSID deviceId) {
        DeviceMonitoring deviceMonitoring = findByIdOrDefault(deviceId);
        return DeviceMonitoringOutput.builder()
                .id(deviceMonitoring.getId().getValue())
                .lastCo2Ppm(deviceMonitoring.getLastCo2Ppm())
                .lastMeasuredAt(deviceMonitoring.getLastMeasuredAt())
                .enabled(deviceMonitoring.getEnabled())
                .alerting(deviceMonitoring.getAlerting())
                .build();

    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable TSID deviceId) {
        DeviceMonitoring deviceMonitoring = findByIdOrDefault(deviceId);
        deviceMonitoring.setEnabled(TRUE);
        deviceMonitoringRepository.saveAndFlush(deviceMonitoring);
    }

    @PutMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable TSID deviceId) {
        DeviceMonitoring deviceMonitoring = findByIdOrDefault(deviceId);
        deviceMonitoring.setEnabled(FALSE);
        // Sem leituras no período desativado, a contagem do tempo acima do limite recomeça
        deviceMonitoring.clearAlertState();
        deviceMonitoringRepository.saveAndFlush(deviceMonitoring);
    }

    private DeviceMonitoring findByIdOrDefault(TSID deviceId) {
        return deviceMonitoringRepository.findById(new DeviceId(deviceId))
                .orElse(DeviceMonitoring.builder()
                        .id(new DeviceId(deviceId))
                        .lastCo2Ppm(null)
                        .lastMeasuredAt(null)
                        .enabled(FALSE)
                        .alerting(FALSE)
                        .build());
    }
}
