package model;

import lombok.Value;

@Value(staticConstructor = "of")
public class Vehicle {
    private Integer id;
    private Integer maxCapacity;
    private Integer actualLoaded;
    private Location location;
}
