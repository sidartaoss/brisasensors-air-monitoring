package com.brisasensors.air.monitoring.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMonitoring {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "BIGINT"))
    private DeviceId id;
    private Double lastCo2Ppm;
    private OffsetDateTime lastMeasuredAt;
    private Boolean enabled;
    private OffsetDateTime exceededSince;
    private Boolean alerting;

    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }

    public boolean isAlerting() {
        return Boolean.TRUE.equals(alerting);
    }

    public void clearAlertState() {
        exceededSince = null;
        alerting = false;
    }
}
