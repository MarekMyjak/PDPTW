package pl.edu.agh.io.pdptw.visualization.model;

import lombok.AllArgsConstructor;
import pl.edu.agh.io.pdptw.model.Location;

import java.util.List;

@AllArgsConstructor
public class VisualizationData {
    String truckId;
    Integer maxCapacity;
    Location location;
    Location startLocation;
    List<VisualizationRoute> routes;
}
