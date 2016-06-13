package pl.edu.agh.io.pdptw.visualization.model;

import lombok.AllArgsConstructor;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.RequestType;

@AllArgsConstructor
public class VisualizationRoute {
    Integer id;
    Location location;
    Integer volume;
    Integer timeWindowStart;
    Integer timeWindowEnd;
    Integer serviceTime;
    Integer realizationTime;
    Integer arrivalTime;
    RequestType type;
}
