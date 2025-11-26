package com.fsv.algasensors.temperature.monitoring.domain.service;

import com.fsv.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.fsv.algasensors.temperature.monitoring.domain.model.SensorId;
import com.fsv.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.fsv.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.fsv.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.fsv.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.fsv.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureMonitoringService {

    private final TemperatureLogRepository temperatureLogRepository;

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> logIgnoredTemperature(temperatureLogData));
    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensorMonitoring) {
        if (sensorMonitoring.isEnabled()) {
            sensorMonitoring.setLastTemperature(temperatureLogData.getValue());
            sensorMonitoring.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensorMonitoring);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .build();

            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature Updated: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperature Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
