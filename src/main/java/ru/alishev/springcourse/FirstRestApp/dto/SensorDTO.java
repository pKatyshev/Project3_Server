package ru.alishev.springcourse.FirstRestApp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SensorDTO {
    @NotNull(message = "sensor_name should not be empty")
    @Size(min = 3, max = 30, message = "size should be 3-30 chars")
    private String name;
}
