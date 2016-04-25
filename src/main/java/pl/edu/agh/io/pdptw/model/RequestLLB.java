package pl.edu.agh.io.pdptw.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class RequestLLB {
    private Integer id;
    private Location location;
    private Integer volume;
    private Integer timeWindowStart;
    private Integer timeWindowEnd;
    private Integer serviceTime;
    private Integer pickup;
    private Integer delivery;
}
