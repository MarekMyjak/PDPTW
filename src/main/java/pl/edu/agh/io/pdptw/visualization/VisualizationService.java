package pl.edu.agh.io.pdptw.visualization;

import com.google.gson.Gson;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.visualization.VisualizationData;
import pl.edu.agh.io.pdptw.model.visualization.VisualizationRoute;

import java.util.LinkedList;
import java.util.List;

public class VisualizationService {

    void makeVisualizationData(Solution solution){
        List<VisualizationData> visualizationData = new LinkedList<>();
        solution.getVehicles().forEach(vehicle -> {
            List<VisualizationRoute> routes = new LinkedList<>();
            vehicle.getRoute().getRequests().forEach(request -> {
                routes.add(new VisualizationRoute(request.getId(), request.getLocation(), request.getVolume(),
                        request.getTimeWindowStart(), request.getTimeWindowEnd(), request.getServiceTime(),
                        request.getRealizationTime(), request.getArrivalTime(), request.getType()));
            });
            visualizationData.add(new VisualizationData(vehicle.getId(), vehicle.getMaxCapacity(),
                    vehicle.getCurrentlyLoaded(), vehicle.getLocation(), vehicle.getStartLocation(),
                    routes));
        });

        Gson gson = new Gson();
        String json = gson.toJson(visualizationData);
        System.out.println(json);
    }
}
