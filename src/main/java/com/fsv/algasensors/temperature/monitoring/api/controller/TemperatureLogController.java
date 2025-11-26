package com.fsv.algasensors.temperature.monitoring.api.controller;

import com.fsv.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.fsv.algasensors.temperature.monitoring.domain.model.SensorId;
import com.fsv.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.fsv.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
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
@RequiredArgsConstructor
@RequestMapping("/api/sensors/{sensorId}/temperatures")
public class TemperatureLogController {

    private final TemperatureLogRepository temperatureLogRepository;

    @GetMapping
    public Page<TemperatureLogData> search(@PathVariable TSID sensorId, @PageableDefault Pageable pageable) {
        Page<TemperatureLog> temperatureLogs = temperatureLogRepository.findAllBySensorId(new SensorId(sensorId), pageable);

        return temperatureLogs.map(temperatureLog ->
            TemperatureLogData.builder()
                    .id(temperatureLog.getId().getValue())
                    .value(temperatureLog.getValue())
                    .registeredAt(temperatureLog.getRegisteredAt())
                    .sensorId(temperatureLog.getSensorId().getValue())
                    .build()
        );
    }
}
