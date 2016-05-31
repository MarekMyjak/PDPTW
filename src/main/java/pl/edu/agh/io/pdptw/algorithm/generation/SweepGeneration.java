package pl.edu.agh.io.pdptw.algorithm.generation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class SweepGeneration implements GenerationAlgorithm {

	@Override
	public Solution generateSolution(List<Request> requestPool,
			List<Vehicle> vehicles, Configuration configuration)
			throws IllegalArgumentException {

		if (requestPool == null) {
			throw new IllegalArgumentException("Pool of requests list is set to NULL");
		}
		if (vehicles == null) {
			throw new IllegalArgumentException("Vehicles list is set to NULL");
		}
		
		InsertionAlgorithm insertionAlg = configuration.getAlgorithms().getInsertionAlgorithm();
		Objective objective = configuration.getAlgorithms().getObjective();
		List<Vehicle> usedVehicles = new LinkedList<>();
		Solution result = new Solution(usedVehicles);
		Location warehouseLocation = vehicles.get(0).getLocation();
		double maxAngleDelta = (2 * Math.PI) / requestPool.size();
		maxAngleDelta = (maxAngleDelta == 0) ? 0.1 : maxAngleDelta;

		/* calculate the polar angle between the warehouse (the start point)
		 * and each request location */
		
		requestPool.forEach(r -> r.getLocation().updatePolarAngle(warehouseLocation));
		
		/* sort requests by the increasing polar angle */
		
		List<PickupRequest> pickupRequests = requestPool.stream()
				.filter(r -> r.getType() == RequestType.PICKUP)
				.map(r -> (PickupRequest) r)
				.sorted((r1, r2) -> {
					double r1Angle = r1.getLocation().getPolarAngle();
					double r2Angle = r2.getLocation().getPolarAngle();
					
					return (r1Angle < r2Angle) 
							? -1 
							: (r1Angle > r2Angle) ? 1 : 0;
				}).collect(Collectors.toCollection(LinkedList::new));
		
		Iterator<Vehicle> vehiclesIt = vehicles.iterator();
		Iterator<PickupRequest> pickupIt = pickupRequests.iterator();
		PickupRequest pickupWithoutVehicle = null;
		boolean requestLeft = true;
		
		while (requestLeft
				&& vehiclesIt.hasNext()) {
			
			boolean insertedSuccessfully = true;
			Vehicle curVehicle = vehiclesIt.next();
			requestLeft = false;
			
			if (pickupWithoutVehicle != null) {
				insertionAlg.insertRequestForVehicle(pickupWithoutVehicle, curVehicle, objective);
				pickupWithoutVehicle = null;
			}
			
			while (insertedSuccessfully
					&& pickupIt.hasNext()) {
				
				PickupRequest curRequest = pickupIt.next();
				insertedSuccessfully = insertionAlg.insertRequestForVehicle(curRequest, curVehicle, objective);
				
				if (!insertedSuccessfully) {
					pickupWithoutVehicle = curRequest;
					requestLeft = true;
				}
			}
			
			usedVehicles.add(curVehicle);
		}
		
		result.setObjectiveValue(objective.calculate(result));
		return result;
	}
}
