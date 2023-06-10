package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstRestApp.models.Sensor;
import ru.alishev.springcourse.FirstRestApp.repositories.SensorRepository;
import ru.alishev.springcourse.FirstRestApp.util.WeatherControllerException;
import ru.alishev.springcourse.FirstRestApp.util.SensorNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    public Sensor findOne(int id) {
        Optional<Sensor> optional = sensorRepository.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new SensorNotFoundException();
        }
    }

    public void save(Sensor sensor) {
        Optional<Sensor> optional = sensorRepository.findByName(sensor.getName());

        if(optional.isPresent()) {
            throw new WeatherControllerException("sensor name must be unique");
        }

        sensorRepository.save(sensor);
    }
}
