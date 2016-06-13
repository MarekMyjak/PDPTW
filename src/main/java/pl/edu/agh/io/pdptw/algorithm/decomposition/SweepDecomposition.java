package pl.edu.agh.io.pdptw.algorithm.decomposition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class SweepDecomposition implements DecompositionAlgorithm {
	
	@Override
	public List<Solution> decompose(Solution solution,
			Configuration configuration) {
		
		final int MAX_VEHICLES = configuration.getMaxVehiclesInGroup();
		List<Vehicle> vehicles = solution.getVehicles();
		Location warehouseLocation = vehicles.get(0).getStartLocation();
		Map<Vehicle, Double> anglesForVehicles = new HashMap<>();
		vehicles.stream()
			.forEach(v -> anglesForVehicles.put(v, 
				Location.calculatePolarAngle(warehouseLocation,
				Location.findCentroid(v.getRoute()
						.getRequests()
						.stream()
						.map(Request::getLocation)
						.collect(Collectors.toList())
				))));
		
		/* we need to sort the vehicles
		 * based on the location of their
		 * routes' centroids */
		
		vehicles = vehicles.stream()
				.sorted((v1, v2) -> (anglesForVehicles.get(v1)
						> anglesForVehicles.get(v2)) ? 1 : -1)
				.collect(Collectors.toList());
		
		/* we assume that that each partial solution
		 * is made of at most MAX_VEHICLES routes */
		
		List<Solution> solutions = new ArrayList<>(solution.getRequests().size() / MAX_VEHICLES);
		double startAngle = (Math.random() * (2 * Math.PI));
		double curAngle = -1.0;
		Iterator<Vehicle> it = vehicles.iterator();
		Vehicle startVehicle = null;
		int skipped = 0;
				
		/* skip the vehicles whose centroids 
		 * make up with the warehouse location
		 * an angle less than the starting one */
		
		while (it.hasNext() && curAngle < startAngle) {
			startVehicle = it.next();
			curAngle = anglesForVehicles.get(startVehicle);
			skipped++;
		}
		
		int vehiclesAddedToCurrentSolution = 1;
		Solution curSolution = new Solution(new LinkedList<>());
		curSolution.getVehicles().add(startVehicle);
		solutions.add(curSolution);
		
		while (it.hasNext()) {
			if (vehiclesAddedToCurrentSolution > MAX_VEHICLES) {
				curSolution = new Solution(new LinkedList<Vehicle>());
				solutions.add(curSolution);
				vehiclesAddedToCurrentSolution = 0;
			}
			
			curSolution.getVehicles().add(it.next());
			vehiclesAddedToCurrentSolution++;
			
		}
		
		/* add all the remaining vehicles */
		
		if (skipped > 0) {
			it = vehicles.iterator();
			int vehiclesIndex = 0;
			
			while (vehiclesIndex < (skipped - 1)) {
				if (vehiclesAddedToCurrentSolution > MAX_VEHICLES) {
					curSolution = new Solution(new LinkedList<Vehicle>());
					solutions.add(curSolution);
					vehiclesAddedToCurrentSolution = 0;
				}
				
				curSolution.getVehicles().add(it.next());
				vehiclesAddedToCurrentSolution++;
				vehiclesIndex++;
				
			}
		}
		
		return solutions;
	}
}
