package com.brisasensors.air.monitoring.domain.service;

import com.brisasensors.air.monitoring.domain.model.DeviceAlert;
import com.brisasensors.air.monitoring.domain.model.DeviceMonitoring;
import com.brisasensors.air.monitoring.domain.model.Reading;
import com.brisasensors.air.monitoring.domain.repository.DeviceAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceAlertService {

    private final DeviceAlertRepository deviceAlertRepository;

    // Alerta quando o CO2 permanece acima do limite por um tempo mínimo; o estado fica no DeviceMonitoring
    public void evaluate(final DeviceMonitoring monitoring, final Reading reading) {
        deviceAlertRepository.findById(monitoring.getId())
                .ifPresentOrElse(
                        alert -> evaluateLimit(alert, monitoring, reading),
                        monitoring::clearAlertState
                );
    }

    private void evaluateLimit(final DeviceAlert alert, final DeviceMonitoring monitoring, final Reading reading) {
        if (reading.getCo2Ppm() <= alert.getMaxCo2Ppm()) {
            if (monitoring.isAlerting()) {
                log.info("Alert cleared: DeviceId: {}, Co2Ppm: {}", reading.getDeviceId(), reading.getCo2Ppm());
            }
            monitoring.clearAlertState();
            return;
        }

        if (monitoring.getExceededSince() == null) {
            monitoring.setExceededSince(reading.getMeasuredAt());
        }

        OffsetDateTime sustainedUntil = monitoring.getExceededSince().plusMinutes(alert.getSustainedMinutes());
        if (!monitoring.isAlerting() && !reading.getMeasuredAt().isBefore(sustainedUntil)) {
            monitoring.setAlerting(true);
            log.info("Alert raised: DeviceId: {}, Co2Ppm: {}, ExceededSince: {}",
                    reading.getDeviceId(), reading.getCo2Ppm(), monitoring.getExceededSince());
        }
    }
}
