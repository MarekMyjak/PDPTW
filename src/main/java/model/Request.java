package model;

import lombok.Value;

@Value(staticConstructor = "of")
public class Request {
    private Location location;
    private Integer demand;
    private Integer timeWindowStart;
    private Integer timeWindowEnd;
    private Integer serviceTime;
    private Integer pickup;
    private Integer delivery;
}
