package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Request {
    private Integer id;
    private Location location;
    private Integer volume;
    private Integer timeWindowStart;
    private Integer timeWindowEnd;
    private Integer serviceTime;
    private Request sibling;
    private RequestType type;
}
