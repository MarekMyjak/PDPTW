package pl.edu.agh.io.pdptw;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;

import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.objective.TotalVehiclesObjective;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.configuration.DefaultConfigReader;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.reader.exception.InvalidFileFormatException;


public class Main {

    public static void main(String[] args) throws IOException {
    	try {
    		DefaultConfigReader configReader = new DefaultConfigReader();
    		List<Configuration> testConfigurations;
    		testConfigurations = configReader.loadConfiguration("resources/test/li_lim_benchmark/config.json");
    		Configuration configuration = testConfigurations.get(0);
    		Vehicle.setScheduler(configuration.getAlgorithms().getScheduler());
    		List<Request> requests = configReader.loadRequests(configuration);
			List<Vehicle> vehicles = configReader.loadVehicles(configuration);
			
			AlgorithmConfiguration algs = configuration.getAlgorithms();
			GenerationAlgorithm generation = algs.getGenerationAlgorithm();
			InsertionAlgorithm insertion = algs.getInsertionAlgorithm();
			Objective objective = algs.getObjective();
			Solution solution = generation.generateSolution(requests, vehicles, insertion, objective);
			
			for (Vehicle v : solution.getVehicles()) {
				System.out.println(v);
			}
			
			System.out.println("requests total: " + requests.size());
			System.out.println("inserted total: " + solution.getRequests().size());
			System.out.println("vehicles used: " + solution.getVehicles().size());
			
		} catch (InvalidFileFormatException | ParseException | IllegalArgumentException e) {
			LoggingUtils.logStackTrace(e);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
			LoggingUtils.error("An error occurred while reading input file");
		}

	}
}
