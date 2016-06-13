package pl.edu.agh.io.pdptw;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import pl.edu.agh.io.pdptw.algorithm.dynamic.RequestDispatcher;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.optimization.DecompositionOptimizer;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.configuration.DefaultConfigReader;
import pl.edu.agh.io.pdptw.configuration.exception.InvalidFileFormatException;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;


public class Main {

    public static void main(String[] args) {
    	try {
    		LoggingUtils.info("Loading configuration data");
    		DefaultConfigReader loader = new DefaultConfigReader();
    		List<Configuration> testConfigurations
    			= loader.loadConfiguration("resources/test/config.json");
    		
    		for (Configuration configuration : testConfigurations) {
    			
    			/* configure logger */
    			LoggingUtils.configure(configuration);
    			
    			Vehicle.setScheduler(configuration.getAlgorithms().getScheduler());
    			List<Request> requests = loader.loadRequests(configuration);
    			List<Vehicle> vehicles = loader.loadVehicles(configuration);
    			
    			AlgorithmConfiguration algs = configuration.getAlgorithms();
    			GenerationAlgorithm generation = algs.getGenerationAlgorithm();
    			Solution solution = null;
    			
    			if (configuration.isDynamic()) {
    				LoggingUtils.info("Dynamic version detected");
    				RequestDispatcher dispatcher = new RequestDispatcher(requests, vehicles, configuration);
    			} else {
    				LoggingUtils.info("Static version detected");
    				solution = generation.generateSolution(requests, vehicles, configuration);
    				LoggingUtils.info("Original objective value: " + solution.getObjectiveValue());
    				DecompositionOptimizer optimizer = new DecompositionOptimizer(solution, configuration);
    				optimizer.startThread();
    			}
    		}

		} catch (InvalidFileFormatException | ParseException | IllegalArgumentException e) {
			LoggingUtils.logStackTrace(e);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
			LoggingUtils.error("An error occurred while reading input file");
		}
	}
}
