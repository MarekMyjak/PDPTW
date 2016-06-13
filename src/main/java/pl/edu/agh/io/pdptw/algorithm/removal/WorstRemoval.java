package pl.edu.agh.io.pdptw.algorithm.removal;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class WorstRemoval implements RemovalAlgorithm {

	@Override
	public Request removeRequestForVehicle(Vehicle vehicle, Configuration configuration) {
		RequestPositions worstPositions = findBestRemovalPositions(vehicle, configuration);
		
		return vehicle.removeRequest(
				worstPositions.getPickupPosition(), worstPositions.getDeliveryPosition());
	}

	@Override
	public RequestPositions findBestRemovalPositions(Vehicle vehicle,
			Configuration configuration) {
		
		Objective objective = configuration.getAlgorithms().getObjective();
		double minObjective = Integer.MAX_VALUE;
		double newObjective = Integer.MAX_VALUE;
		RequestPositions worstPositions = RequestPositions.createDefault();
		Route route = vehicle.getRoute();
		List<PickupRequest> pickupRequests = route.getRequests()
				.stream()
				.filter(r -> r.getType() == RequestType.PICKUP
						&& !vehicle.getServedRequestsIds().contains(r.getId()))
				.map(r -> (PickupRequest) r)
				.collect(Collectors.toList());
		
		/* looking for the requests pair
		 * such that after its removal
		 * the new objective value is least */
		
		for (PickupRequest pickup : pickupRequests) {
			RequestPositions positions = vehicle.removeRequest(pickup);
			newObjective = objective.calculateForVehicle(vehicle);
			positions.setObjectiveValue(newObjective);
			
			vehicle.insertRequest(pickup, positions.getPickupPosition(), positions.getDeliveryPosition());
			
			if (newObjective < minObjective) {
				worstPositions = positions;
			}
		}
		
		return worstPositions;
	}

	@Override
	public Request removeRequestFromSolution(Solution solution,
			Configuration configuration) {
		
		double minValue = Integer.MAX_VALUE;
		RequestPositions worstPosition = RequestPositions.createDefault();
		Vehicle worstVehicle = null;
		Request worstRequest = null;
		
		for (Vehicle vehicle : solution.getVehicles()) {
			RequestPositions curPosition = 
					findBestRemovalPositions(vehicle, configuration);	
			
			if (curPosition.getObjectiveValue() < minValue) {
				worstPosition = curPosition;
				worstVehicle = vehicle;
				minValue = curPosition.getObjectiveValue();
			}
		}
		
		if (minValue < Integer.MAX_VALUE) {
			worstRequest = worstVehicle.removeRequest( 
					worstPosition.getPickupPosition(), worstPosition.getDeliveryPosition());
		}
		
		return worstRequest;
	}

}
