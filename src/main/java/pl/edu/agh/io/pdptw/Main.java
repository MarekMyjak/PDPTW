package pl.edu.agh.io.pdptw;

import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.configuration.DefaultConfigReader;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.*;
import pl.edu.agh.io.pdptw.model.visualization.VisualizationData;
import pl.edu.agh.io.pdptw.model.visualization.VisualizationRoute;
import pl.edu.agh.io.pdptw.reader.exception.InvalidFileFormatException;
import pl.edu.agh.io.pdptw.visualization.VisualizationService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
    	try {
    		DefaultConfigReader loader = new DefaultConfigReader();
    		List<Configuration> testConfigurations;
    		testConfigurations = loader.loadConfiguration("resources/test/li_lim_benchmark/config.json");
    		Configuration configuration = testConfigurations.get(0);
    		Vehicle.setScheduler(configuration.getAlgorithms().getScheduler());
    		List<Request> requests = loader.loadRequests(configuration);
			List<Vehicle> vehicles = loader.loadVehicles(configuration);
			
			AlgorithmConfiguration algs = configuration.getAlgorithms();
			GenerationAlgorithm generation = algs.getGenerationAlgorithm();
			Solution solution = generation.generateSolution(requests, vehicles, configuration);
			Objective objective = algs.getObjective();

			solution.getVehicles().forEach(System.out::println);
			
			System.out.println("requests total: " + requests.size());
			System.out.println("inserted total: " + solution.getRequests().size());
			System.out.println("vehicles used: " + solution.getVehicles().size());
			System.out.println("Objective value :" + objective.calculate(solution));
			
			OptimizationAlgorithm optimization = configuration.getAlgorithms()
					.getOptimizationAlgorithm();

			VisualizationService service = new VisualizationService();
			service.makeVisualizationData(solution);

		} catch (InvalidFileFormatException | ParseException | IllegalArgumentException e) {
			LoggingUtils.logStackTrace(e);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
			LoggingUtils.error("An error occurred while reading input file");
		}

	}
}
