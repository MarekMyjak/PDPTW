package pl.edu.agh.io.pdptw.algorithm.generation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class GreedyGeneration implements GenerationAlgorithm {

	@Override
	public Solution generateSolution(List<Request> requestPool,
			List<Vehicle> vehicles, Configuration configuration)
			throws IllegalArgumentException {
		
		InsertionAlgorithm insertion = configuration.getAlgorithms().getInsertionAlgorithm(); 
		Iterator<Vehicle> vehiclesIt = vehicles.iterator();
		Iterator<PickupRequest> pickupsIt = requestPool.stream()
				.filter(r -> r.getType() == RequestType.PICKUP)
				.map(r -> (PickupRequest) r)
				.collect(Collectors.toList())
				.iterator();
		Solution solution = new Solution(new LinkedList<Vehicle>());
		Vehicle curVehicle = vehiclesIt.next();
		
		while (pickupsIt.hasNext() 
				&& vehiclesIt.hasNext()) {
			
			PickupRequest pickup = pickupsIt.next();
			
			if (!insertion.insertRequestForVehicle(pickup, curVehicle, configuration)) {
				solution.getVehicles().add(curVehicle);
				curVehicle = vehiclesIt.next();
				curVehicle.insertRequest(pickup, 0, 1);
			}
		}
		
		solution.getVehicles().add(curVehicle);
		
		/* update the objective value */
		solution.updateOjectiveValue(configuration.getAlgorithms().getObjective());
		
		return solution;
	}


}
