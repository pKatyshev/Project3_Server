package ru.alishev.springcourse.FirstRestApp.util;

public class WeatherControllerException extends RuntimeException{
    public WeatherControllerException(String message) {
        super(message);
    }
}
