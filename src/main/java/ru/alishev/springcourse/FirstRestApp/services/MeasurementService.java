package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.models.Sensor;
import ru.alishev.springcourse.FirstRestApp.repositories.MeasurementRepository;
import ru.alishev.springcourse.FirstRestApp.repositories.SensorRepository;
import ru.alishev.springcourse.FirstRestApp.util.SensorNotFoundException;
import ru.alishev.springcourse.FirstRestApp.util.WeatherControllerException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public Measurement findOne(int id) {
        Optional<Measurement> optional = measurementRepository.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new WeatherControllerException("Measurement with id={" + id + "} not found!");
        }
    }

    public List<Measurement> getAllRainingDays() {
        System.out.println(measurementRepository.findByRainingTrue());
        return measurementRepository.findByRainingTrue();
    }

    public void save(Measurement measurement) {
            enrichMeasurement(measurement);
            measurementRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement) {
        Optional<Sensor> optional = sensorRepository.findByName(measurement.getSensor().getName());
        if (optional.isPresent()){
            measurement.setSensor(optional.get());
            measurement.setTime(LocalDateTime.now());
        } else {
            throw new SensorNotFoundException();
        }
    }
}
