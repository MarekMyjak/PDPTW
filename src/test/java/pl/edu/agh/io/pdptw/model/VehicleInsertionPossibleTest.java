package pl.edu.agh.io.pdptw.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.configuration.DefaultConfigReader;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.reader.exception.InvalidFileFormatException;

public class VehicleInsertionPossibleTest {

	@Test
	public void test() {
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
			Vehicle vehicle = vehicles.get(0);
			List<Integer> soughtIds = Arrays.asList(57, 9, 56, 6);
			
			PickupRequest r57 = (PickupRequest)requests.stream()
					.filter(r -> r.getId().equals(57))
					.collect(Collectors.toList())
					.get(0);
			PickupRequest r56 = (PickupRequest)requests.stream()
					.filter(r -> r.getId().equals(56))
					.collect(Collectors.toList())
					.get(0);
			PickupRequest r9 = (PickupRequest)requests.stream()
					.filter(r -> r.getId().equals(9))
					.collect(Collectors.toList())
					.get(0);
			PickupRequest r6 = (PickupRequest)requests.stream()
					.filter(r -> r.getId().equals(6))
					.collect(Collectors.toList())
					.get(0);

			insertion.insertRequestForVehicle(r6, vehicle, objective);
			insertion.insertRequestForVehicle(r56, vehicle, objective);
			System.out.println(vehicle);
		} catch (InvalidFileFormatException | ParseException | IllegalArgumentException e) {
			LoggingUtils.logStackTrace(e);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
			LoggingUtils.error("An error occurred while reading input file");
		}
	}

}
