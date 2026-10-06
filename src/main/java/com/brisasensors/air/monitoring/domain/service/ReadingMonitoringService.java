package com.brisasensors.air.monitoring.domain.service;

import com.brisasensors.air.monitoring.domain.model.DeviceMonitoring;
import com.brisasensors.air.monitoring.domain.model.Reading;
import com.brisasensors.air.monitoring.domain.repository.DeviceMonitoringRepository;
import com.brisasensors.air.monitoring.domain.repository.ReadingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadingMonitoringService {

    private final DeviceMonitoringRepository deviceMonitoringRepository;
    private final ReadingRepository readingRepository;
    private final DeviceAlertService deviceAlertService;

    @Transactional
    public void processReading(final Reading reading) {
        deviceMonitoringRepository.findById(reading.getDeviceId())
                .ifPresentOrElse(
                        monitoring -> handleDeviceMonitoring(reading, monitoring),
                        () -> logIgnoredReading(reading)
                );
    }

    private void handleDeviceMonitoring(final Reading reading, final DeviceMonitoring monitoring) {
        if (!monitoring.isEnabled()) {
            logIgnoredReading(reading);
            return;
        }

        readingRepository.save(reading);

        // Leitura mais antiga que o valor atual entra no histórico, mas não o substitui nem reavalia o alerta
        if (monitoring.getLastMeasuredAt() != null && reading.getMeasuredAt().isBefore(monitoring.getLastMeasuredAt())) {
            log.info("Late reading stored: DeviceId: {}, Co2Ppm: {}, MeasuredAt: {}",
                    reading.getDeviceId(), reading.getCo2Ppm(), reading.getMeasuredAt());
            return;
        }

        monitoring.setLastCo2Ppm(reading.getCo2Ppm());
        monitoring.setLastMeasuredAt(reading.getMeasuredAt());
        deviceAlertService.evaluate(monitoring, reading);
        deviceMonitoringRepository.save(monitoring);
        log.info("Reading updated for DeviceId: {}, Co2Ppm: {}", reading.getDeviceId(), reading.getCo2Ppm());
    }

    private void logIgnoredReading(final Reading reading) {
        log.info("Reading ignored for DeviceId: {}, Co2Ppm: {}", reading.getDeviceId(), reading.getCo2Ppm());
    }
}
