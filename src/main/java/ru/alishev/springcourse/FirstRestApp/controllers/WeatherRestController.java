package ru.alishev.springcourse.FirstRestApp.controllers;

import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.MeasurementDTO;
import ru.alishev.springcourse.FirstRestApp.dto.MeasurementsResponse;
import ru.alishev.springcourse.FirstRestApp.dto.SensorDTO;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.models.Sensor;
import ru.alishev.springcourse.FirstRestApp.services.MeasurementService;
import ru.alishev.springcourse.FirstRestApp.services.SensorService;
import ru.alishev.springcourse.FirstRestApp.util.SensorErrorResponse;
import ru.alishev.springcourse.FirstRestApp.util.WeatherControllerException;
import ru.alishev.springcourse.FirstRestApp.util.SensorNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
@Log
@RestController
public class WeatherRestController {
    private final ModelMapper modelMapper;
    private final SensorService sensorService;
    private final MeasurementService measurementService;

    public WeatherRestController(ModelMapper modelMapper, SensorService sensorService, MeasurementService measurementService) {
        this.modelMapper = modelMapper;
        this.sensorService = sensorService;
        this.measurementService = measurementService;
    }

    @PostMapping("/sensors/registration")
    public ResponseEntity<HttpStatus> registrationSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String message = collectErrors(bindingResult);
            throw new WeatherControllerException(message);
        }

        sensorService.save(converToSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/measurements/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = collectErrors(bindingResult);
            throw new WeatherControllerException(message);
        }
        measurementService.save(convertToMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/measurements")
    public MeasurementsResponse getMeasurements() {
        return new MeasurementsResponse(measurementService.findAll()
                .stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("measurements/rainyDaysCount")
    public long getAllRaining() {
        return measurementService.getAllRainingDays()
                .stream()
                .map(this::convertToMeasurementDTO)
                .count();
    }


    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private Sensor converToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    @GetMapping("/measurement")
    public MeasurementDTO getMeasurement() {
        return convertToMeasurementDTO(measurementService.findOne(1));
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handle(SensorNotFoundException e) {
        SensorErrorResponse response = new SensorErrorResponse(
               "sensor not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handle(WeatherControllerException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String collectErrors(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError err : errors) {
            message.append(err.getField()).append(" - ")
                    .append(err.getDefaultMessage()).append(";");
        }
        return message.toString();
    }

}
