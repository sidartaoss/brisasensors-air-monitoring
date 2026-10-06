package com.brisasensors.air.monitoring.domain.repository;

import com.brisasensors.air.monitoring.domain.model.DeviceAlert;
import com.brisasensors.air.monitoring.domain.model.DeviceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceAlertRepository extends JpaRepository<DeviceAlert, DeviceId> {
}
