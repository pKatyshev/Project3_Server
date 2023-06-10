package ru.alishev.springcourse.FirstRestApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MeasurementDTO {

    @NotNull(message = "value should not be empty")
    @Min(value = -100, message = "is to cold")
    @Max(value = 100, message = "is to hot!")
    private Double value;

    @NotNull(message = "raining should not be empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean raining;

    @NotNull(message = "sensor_name should not be empty")
    private SensorDTO sensor;

    @Override
    public String toString() {
        return "MeasurementDTO{" +
                "value=" + value +
                ", raining=" + raining +
                ", sensor=" + sensor.getName() +
                '}';
    }
}
