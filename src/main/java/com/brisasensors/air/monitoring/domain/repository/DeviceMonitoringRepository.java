package com.brisasensors.air.monitoring.domain.repository;

import com.brisasensors.air.monitoring.domain.model.DeviceId;
import com.brisasensors.air.monitoring.domain.model.DeviceMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceMonitoringRepository extends JpaRepository<DeviceMonitoring, DeviceId> {
}
