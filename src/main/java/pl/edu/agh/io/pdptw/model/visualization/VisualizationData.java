package pl.edu.agh.io.pdptw.model.visualization;

import lombok.AllArgsConstructor;
import pl.edu.agh.io.pdptw.model.Location;

import java.util.List;

@AllArgsConstructor
public class VisualizationData {
    String truckId;
    Integer maxCapacity;
    Integer currentlyLoaded;
    Location location;
    Location startLocation;
    List<VisualizationRoute> routes;
}
