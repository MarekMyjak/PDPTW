package pl.edu.agh.io.pdptw.algorithm.insertion;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.*;

import java.util.List;

public class GreedyInsertion implements InsertionAlgorithm {
	
	/* The returned value is equal to the position
     * of the newly inserted pickup request in the
     * requests pool */

	@Override
	public boolean insertRequestForVehicle(PickupRequest pickup, Vehicle vehicle, Objective objective) {
		RequestPositions bestPosition = 
				findBestInsertionPositions(pickup, vehicle, objective);
		boolean inserted = false;
		
		if (bestPosition.getPickupPosition() != Integer.MAX_VALUE
				&& bestPosition.getDeliveryPosition() != Integer.MAX_VALUE) {
			
			vehicle.insertRequest(pickup, bestPosition.getPickupPosition(), 
					bestPosition.getDeliveryPosition());
			inserted = true;
		}
		
		return inserted;
	}

	@Override
	public RequestPositions findBestInsertionPositions(
			PickupRequest pickup, Vehicle vehicle, Objective objective) {
		
		int pickupPosition = Integer.MAX_VALUE;
		int deliveryPosition = Integer.MAX_VALUE;
		double minObjective = Integer.MAX_VALUE;
		double newObjective = Integer.MAX_VALUE;
		List<Request> requests = vehicle.getRoute().getRequests();
		RequestPositions bestPositions = RequestPositions.createDefault();
		/* looking for the best position
		 * to add the new pickup request */
		
		for (int pPos = 0; pPos <= requests.size(); pPos++) {
			for (int dPos = pPos + 1; dPos <= requests.size() + 1; dPos++) {
				
				if (vehicle.isInsertionPossible(pickup, pPos, dPos)) {
					
					vehicle.insertRequest(pickup, pPos, dPos);
					newObjective = objective.calculateForVehicle(vehicle);
					
					if (newObjective < minObjective) {
						minObjective = newObjective;
						pickupPosition = pPos;
						deliveryPosition = dPos;
					}
					
					vehicle.removeRequest(pickup);
				}
			}
		}
		
		if (pickupPosition != Integer.MAX_VALUE
				&& deliveryPosition != Integer.MAX_VALUE) {
			bestPositions = new RequestPositions(pickupPosition, deliveryPosition, minObjective);
		}
		
		return bestPositions;
	}

	@Override
	public boolean insertRequestToSolution(PickupRequest pickup,
			Solution solution, Objective objective) {
		
		double minValue = Integer.MAX_VALUE;
		RequestPositions bestPosition = RequestPositions.createDefault();
		Vehicle bestVehicle = null;
		boolean inserted = false;
		
		for (Vehicle vehicle : solution.getVehicles()) {
			RequestPositions curPosition = 
					findBestInsertionPositions(pickup, vehicle, objective);		
			
			if (curPosition.getObjectiveValue() < minValue) {
				bestPosition = curPosition;
				bestVehicle = vehicle;
				minValue = curPosition.getObjectiveValue();
			}
		}
		
		if (minValue < Integer.MAX_VALUE) {
			bestVehicle.insertRequest(pickup, 
					bestPosition.getPickupPosition(), bestPosition.getDeliveryPosition());
			inserted = true;
		}
		
		return inserted;
	}

}
