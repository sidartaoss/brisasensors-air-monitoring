package com.brisasensors.air.monitoring.api.controller;

import com.brisasensors.air.monitoring.api.model.DeviceAlertInput;
import com.brisasensors.air.monitoring.api.model.DeviceAlertOutput;
import com.brisasensors.air.monitoring.domain.model.DeviceAlert;
import com.brisasensors.air.monitoring.domain.model.DeviceId;
import com.brisasensors.air.monitoring.domain.repository.DeviceAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/devices/{deviceId}/alert")
@RequiredArgsConstructor
public class DeviceAlertController {

    private final DeviceAlertRepository deviceAlertRepository;

    @GetMapping
    public DeviceAlertOutput getDetail(@PathVariable TSID deviceId) {
        DeviceAlert deviceAlert = deviceAlertRepository.findById(new DeviceId(deviceId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return convertToModel(deviceAlert);
    }

    @PutMapping
    public DeviceAlertOutput createOrUpdate(
            @PathVariable TSID deviceId,
            @RequestBody DeviceAlertInput input) {
        if (input.getMaxCo2Ppm() == null || input.getMaxCo2Ppm() <= 0
                || input.getSustainedMinutes() == null || input.getSustainedMinutes() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid alert configuration");
        }

        DeviceAlert deviceAlert = deviceAlertRepository.findById(new DeviceId(deviceId))
                .orElse(DeviceAlert.builder()
                        .id(new DeviceId(deviceId))
                        .build());

        deviceAlert.setMaxCo2Ppm(input.getMaxCo2Ppm());
        deviceAlert.setSustainedMinutes(input.getSustainedMinutes());

        deviceAlert = deviceAlertRepository.saveAndFlush(deviceAlert);
        return convertToModel(deviceAlert);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID deviceId) {
        DeviceAlert deviceAlert = deviceAlertRepository.findById(new DeviceId(deviceId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        deviceAlertRepository.delete(deviceAlert);
    }

    private DeviceAlertOutput convertToModel(DeviceAlert deviceAlert) {
        return DeviceAlertOutput.builder()
                .id(deviceAlert.getId().getValue())
                .maxCo2Ppm(deviceAlert.getMaxCo2Ppm())
                .sustainedMinutes(deviceAlert.getSustainedMinutes())
                .build();
    }
}
