package pl.edu.agh.io.pdptw.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pl.edu.agh.io.pdptw.configuration.exception.InvalidFileFormatException;
import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Vehicle;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DefaultConfigReader implements ConfigReader {
	private static JSONParser parser = new JSONParser();
	
	public List<Configuration> loadConfiguration(String configFilePath) 
			throws IllegalArgumentException, IOException, ParseException {
		List<Configuration> configurations = new ArrayList<>();
		
		try (
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(configFilePath)))
		) {
			StringBuilder builder = new StringBuilder();
			String line;
			
			while ((line = br.readLine()) != null) {
				builder.append(line + "\n");
			}
			
			JSONObject jsonConfig = (JSONObject) parser.parse(builder.toString());
			JSONArray tests = (JSONArray) jsonConfig.get("tests");

			for (int i = 0; i < tests.size(); i++) {
				JSONObject test = (JSONObject) tests.get(i);
				String requestsPath = (String) test.get("requestsPath");
				String vehiclesPath = (String) test.get("vehiclesPath");
				String outputPath = (String) test.get("outputPath");
				boolean isDynamic = (boolean) test.get("dynamic");
				int iterations = ((Long) test.get("iterations")).intValue();
				int decompositionCycles = ((Long) test.get("decompositionCycles")).intValue();
				int iterationsPerDecomposition = ((Long) test.get("iterationsPerDecomposition")).intValue();
				int maxVehiclesInGroup = ((Long) test.get("maxVehiclesInGroup")).intValue();
				JSONObject algorithms = (JSONObject) test.get("algorithms");

				String generationAlgorithm = (String) algorithms.get("generation");
				String insertionAlgorithm = (String) algorithms.get("insertion");
				String removalAlgorithm = (String) algorithms.get("removal");
				String optimizationAlgorithm = (String) algorithms.get("optimization");
				String objective = (String) algorithms.get("objective");
				String scheduler = (String) algorithms.get("scheduler");
				String decompositionAlgorithm = (String) algorithms.get("decomposition");

				AlgorithmDescription description = new AlgorithmDescription(
					generationAlgorithm,
					insertionAlgorithm,
					removalAlgorithm,
					optimizationAlgorithm,
					objective,
					scheduler,
					decompositionAlgorithm);

				Injector injector = Guice.createInjector(new AlgorithmModule(description));
				AlgorithmConfiguration algorithmConfig = injector.getInstance(AlgorithmConfiguration.class);
				Configuration configuration = new Configuration(
						requestsPath,
						vehiclesPath,
						outputPath,
						isDynamic,
						iterations,
						decompositionCycles,
						iterationsPerDecomposition,
						maxVehiclesInGroup,
						new Location(0, 0),
						algorithmConfig);
				configurations.add(configuration);
			}
		}
		
		return configurations;
	}

	@Override
	public List<Request> loadRequests(Configuration configuration) 
			throws IOException, InvalidFileFormatException {
		
		String requestsFilePath = configuration.getRequestsPath();
		File requestsFile = new File(requestsFilePath);
		int lineCounter = 0;
		List<Request> result = new ArrayList<>();
		Map<String, Request> siblings = new HashMap<>();
		Map<String, Integer> values = new HashMap<>();
		String[] parameterNames = {
			"id", "x", "y", "volume", 
			"timeWindowStart", "timeWindowEnd", "serviceTime",
			"pickupRequestId", "deliveryRequestId"
		};
		
		try (
			Scanner sc = new Scanner(requestsFile)
		) {
			int vehiclesNo = sc.nextInt();
			int totalCapacity = sc.nextInt();
			int vehicleSpeed = sc.nextInt();
			
			/* skip meta data about the vehicles */
			if (sc.hasNextLine()) {
				sc.nextLine();
				lineCounter++;
			}
			
			/* read the warehouse location data */
		    for (int i = 0; i < parameterNames.length && sc.hasNextInt(); i++) {
	    		String parameterName = parameterNames[i];
	    		values.put(parameterName, sc.nextInt());
			}
		    
		    configuration.setWarehouseLocation(new Location(values.get("x"), values.get("y")));
			
			if (sc.hasNextLine()) {
				sc.nextLine();
				lineCounter++;
			}
			
			while (sc.hasNext()) {
				int readValuesNo = 0;
				Request request;
			    
			    for (int i = 0; i < parameterNames.length && sc.hasNextInt(); i++) {
		    		String parameterName = parameterNames[i];
		    		values.put(parameterName, sc.nextInt());
		    		readValuesNo++;
				}
			    
			    if (readValuesNo != parameterNames.length) {
			    	throw new InvalidFileFormatException(
			    			"Invalid requests file format(" + lineCounter + ")"); 
			    }
			    
			    int id = values.get("id");
			    int x = values.get("x");
			    int y = values.get("y");
			    Location location = new Location(x, y);
			    int volume = values.get("volume");
			    int timeWindowStart = values.get("timeWindowStart");
			    int timeWindowEnd = values.get("timeWindowEnd");
			    int serviceTime = values.get("serviceTime");
			    int pickupRequestId = values.get("pickupRequestId");
			    int deliveryRequestId = values.get("deliveryRequestId");
			    int siblingId;
			    
			    if (volume >= 0 
		    		&& pickupRequestId == 0
		    		&& deliveryRequestId > 0) {
			    	
			    	request = new PickupRequest(id, location,
			    			volume, timeWindowStart, timeWindowEnd, serviceTime);
			    	siblingId = deliveryRequestId;
			    	
			    } else if (volume < 0 
		    		&& pickupRequestId > 0
		    		&& deliveryRequestId == 0) {
			    	
			    	request = new DeliveryRequest(id, location,
			    			volume, timeWindowStart, timeWindowEnd, serviceTime);
			    	siblingId = pickupRequestId;
			    } else {
			    	throw new InvalidFileFormatException(
			    			"Invalid request data(" + lineCounter + ", id: " + id + ")");
			    }
			    
			    /* add newly created request to the
			     * final request pool */
			    result.add(request);
			    	
			    if (siblings.containsKey("" + siblingId)) {
			    	Request sibling = siblings.get("" + siblingId);
			    	
			    	try {
			    		sibling.setSibling(request);
			    		request.setSibling(sibling);
			    		siblings.remove("" + siblingId);
					} catch (IllegalArgumentException e) {
						throw new InvalidFileFormatException(
								"Invalid sibling data (" + lineCounter + ", id: " + id 
								+ "sibling id: " + siblingId + ")");
					}
			    } else {
			    	siblings.put("" + id, request);
			    }
			    
			    lineCounter++;
			}
		}
		
		if (siblings.keySet().size() > 0) {
			throw new InvalidFileFormatException(
					"No sibling requests found for the following ones: " + siblings.keySet());
		}
		
		/* If it's a dynamic problem we need to update
		 * the arrival time values for each pickup-delivery requests pair.
		 * It is assumed that arrival times for each pair is stored
		 * in a file with path just like the path to the file storing 
		 * requests data but with additional ".arrival_times" suffix*/
		
		if (configuration.isDynamic()) {
			File arrivalTimesFile = new File(requestsFilePath + ".arrival_times");
			List<Request> pickupRequests = result.stream()
					.filter(r -> r.getType() == RequestType.PICKUP)
					.collect(Collectors.toList());
			lineCounter = 0;
			
			try (
				Scanner sc = new Scanner(arrivalTimesFile)
			) {
				while (sc.hasNextInt()) {
					int arrivalTime = sc.nextInt();
					Request curRequest = pickupRequests.get(lineCounter);
					curRequest.setArrivalTime(arrivalTime);
					curRequest.getSibling().setArrivalTime(arrivalTime);
					lineCounter++;
				}
			}
		}
		
		return result;
	}

	@Override
	public List<Vehicle> loadVehicles(Configuration configuration)
		throws IOException, ParseException {
		
		String vehiclesFilePath = configuration.getVehiclesPath();
		List<Vehicle> result = new ArrayList<>();
		try (
			InputStreamReader in = new InputStreamReader(
				new FileInputStream(vehiclesFilePath))
		) {
			JSONObject vehiclesConfig = (JSONObject) parser.parse(in);
			JSONArray vehicles = (JSONArray) vehiclesConfig.get("vehicles");
			
			for (int i = 0; i < vehicles.size(); i++) {
				JSONObject vehicleData = (JSONObject) vehicles.get(i);
				String id = (String) vehicleData.get("id");
				int maxCapacity = ((Long) vehicleData.get("maxCapacity")).intValue();
				
				/* JSONObject locationJson = (JSONObject) vehicleData.get("startLocation");
				 *  int x = ((Long) locationJson.get("x")).intValue();
				 *	int y = ((Long) locationJson.get("y")).intValue();
				 *	Location startLocation = new Location(x, y); */
				
				/* as we use the Li & Lim benchmark
				 * we set the start location of each vehicle
				 * to the warehouse location (we assume
				 * that all vehicles start from there) */

				Location startLocation = configuration.getWarehouseLocation();
				Vehicle vehicle = new Vehicle(id, maxCapacity, startLocation);
				result.add(vehicle);
			}
		}
		
		return result;
	}
}
