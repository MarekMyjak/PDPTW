package pl.edu.agh.io.pdptw.visualization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.visualization.VisualizationData;
import pl.edu.agh.io.pdptw.model.visualization.VisualizationRoute;

import com.google.gson.Gson;

public class VisualizationService {

    public void makeVisualizationData(Solution solution, Configuration configuration) throws IOException {
        List<VisualizationData> visualizationData = new LinkedList<>();
        solution.getVehicles().forEach(vehicle -> {
            List<VisualizationRoute> routes = new LinkedList<>();
            vehicle.getRoute().getRequests().forEach(request -> routes.add(new VisualizationRoute(request.getId(), request.getLocation(), request.getVolume(),
                    request.getTimeWindowStart(), request.getTimeWindowEnd(), request.getServiceTime(),
                    request.getRealizationTime(), request.getArrivalTime(), request.getType())));
            visualizationData.add(new VisualizationData(vehicle.getId(), vehicle.getMaxCapacity(),
                   vehicle.getLocation(), vehicle.getStartLocation(), routes));
        });

        Gson gson = new Gson();
        String json = gson.toJson(visualizationData);
        String[] requestsPathElements = configuration.getRequestsPath().split("/");
        String fileName = requestsPathElements[requestsPathElements.length - 1]  
				+ "_" + configuration.getIterations()
				+ "_" + configuration.getAlgorithms()
					.getGenerationAlgorithm().getClass().getSimpleName();
        
        LoggingUtils.info("Saving solution data under: " + configuration.getOutputPath() + fileName);
        File outputFile = new File(configuration.getOutputPath() + fileName);
        
        
        if (!outputFile.exists()) {
        	outputFile.createNewFile();
        }
        
        try (
			PrintWriter out = new PrintWriter(outputFile);	
		) {
        	out.println(json);
        }
    }
}
