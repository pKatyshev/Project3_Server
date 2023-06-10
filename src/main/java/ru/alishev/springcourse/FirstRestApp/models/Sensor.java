package ru.alishev.springcourse.FirstRestApp.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "sensor_name should not be empty")
    @Size(min = 3, max = 30, message = "size should be 3-30 chars")
    private String name;


    @OneToMany(mappedBy = "sensor")
    private List<Measurement> measurements;

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


