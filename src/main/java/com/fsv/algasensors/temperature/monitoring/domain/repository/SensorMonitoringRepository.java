package com.fsv.algasensors.temperature.monitoring.domain.repository;

import com.fsv.algasensors.temperature.monitoring.domain.model.SensorId;
import com.fsv.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}
