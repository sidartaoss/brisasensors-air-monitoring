package com.brisasensors.air.monitoring.domain.repository;

import com.brisasensors.air.monitoring.domain.model.DeviceId;
import com.brisasensors.air.monitoring.domain.model.Reading;
import com.brisasensors.air.monitoring.domain.model.ReadingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<Reading, ReadingId> {
    Page<Reading> findAllByDeviceId(DeviceId deviceId, Pageable pageable);
}
