package com.brisasensors.air.monitoring.api.controller;

import com.brisasensors.air.monitoring.api.model.ReadingOutput;
import com.brisasensors.air.monitoring.domain.model.DeviceId;
import com.brisasensors.air.monitoring.domain.model.Reading;
import com.brisasensors.air.monitoring.domain.repository.ReadingRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices/{deviceId}/readings")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingRepository readingRepository;

    @GetMapping
    public Page<ReadingOutput> search(
            @PathVariable TSID deviceId,
            @PageableDefault Pageable pageable
    ) {
        Page<Reading> readings = readingRepository.findAllByDeviceId(new DeviceId(deviceId), pageable);
        return readings.map(reading -> ReadingOutput.builder()
                .id(reading.getId().getValue())
                .deviceId(reading.getDeviceId().getValue())
                .measuredAt(reading.getMeasuredAt())
                .registeredAt(reading.getRegisteredAt())
                .co2Ppm(reading.getCo2Ppm())
                .build());
    }
}
