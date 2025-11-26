package com.fsv.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.fsv.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.fsv.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import com.fsv.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.fsv.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.fsv.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final SensorAlertService sensorAlertService;

    private final TemperatureMonitoringService temperatureMonitoringService;

    @SneakyThrows
    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    public void handleProcesTemperature(@Payload TemperatureLogData temperatureLogData) {

        temperatureMonitoringService.processTemperatureReading(temperatureLogData);

//        Thread.sleep(Duration.ofSeconds(5));
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    public void handleAlerting(@Payload TemperatureLogData temperatureLogData) {

        sensorAlertService.handleAlert(temperatureLogData);

        Thread.sleep(Duration.ofSeconds(5));
    }
}
