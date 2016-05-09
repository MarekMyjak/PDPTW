package pl.edu.agh.io.pdptw.algorithm.insertion;

import java.util.List;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class GreedyInsertion implements InsertionAlgorithm {

    public int insertRequest(PickupRequest request, Solution solution, Objective objective) {
        Integer minRouteId = 0;
        Integer minPosId = 0;
        Integer minReqId = 0;
        Integer minDif = null;
        //TODO

        return 0;
    }

    /* The returned value is equal to the position 
     * of the newly insrted pickup request in the 
     * requests pool */
    
	@Override
	public int insertRequestToVehicleRoute(PickupRequest pickup, Vehicle vehicle, Objective objective) {
		Request delivery = pickup.getSibling();
		int pickupPosition = Integer.MIN_VALUE;
		int deliveryPosition = Integer.MIN_VALUE;
		double minObjective = Integer.MAX_VALUE;
		double newObjective = Integer.MAX_VALUE;
		Route route = vehicle.getRoute();
		List<Request> pool = route.getRequests();
		
		/* looking for the best position
		 * to add the new pickup request */
		
		for (int i = 0; i <= pool.size(); i++) {
			if (vehicle.insertRequest(i, pickup)) {
				newObjective = objective.calculateForRoute(route);
				pool.remove(i);
				
				if (newObjective < minObjective) {
					minObjective = newObjective;
					pickupPosition = i;
				}
			} else {
				System.out.println("SKIPPED ins " + i);
			}
		}
		
		if (pickupPosition >= 0) {
			pool.add(pickupPosition, pickup);
			
			/* looking for the best position for the delivery
			 * request. 
			 * Attention: position of the delivery request must be
			 * greater than the position of the pickup request!
			 * (it is not possible to deliver the package before
			 * picking it up!) */
			
			minObjective = Integer.MAX_VALUE;
			
			for (int i = pickupPosition + 1; i <= pool.size(); i++) {
				if (vehicle.insertRequest(i, delivery)) {
					newObjective = objective.calculateForRoute(route);
					pool.remove(i);
					
					if (newObjective < minObjective) {
						minObjective = newObjective;
						deliveryPosition = i;
					}
				}
			}
			
			if (deliveryPosition > 0) {
				pool.add(deliveryPosition, delivery);
			} else {
				
				/* remove the previously inserted pickup request */
				
				pool.remove(pickupPosition);
				pickupPosition = Integer.MIN_VALUE;
			}
		}
		
		return pickupPosition;
	}

}
