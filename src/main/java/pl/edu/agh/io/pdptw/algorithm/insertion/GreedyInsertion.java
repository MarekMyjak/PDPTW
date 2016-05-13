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
     * requests requests */
    
	@Override
	public int insertRequestToVehicleRoute(PickupRequest pickup, Vehicle vehicle, Objective objective) {
		int pickupPosition = Integer.MIN_VALUE;
		int deliveryPosition = Integer.MIN_VALUE;
		double minObjective = Integer.MAX_VALUE;
		double newObjective = Integer.MAX_VALUE;
		Route route = vehicle.getRoute();
		List<Request> requests = route.getRequests();
		
		/* looking for the best position
		 * to add the new pickup request */
		
		for (int pPos = 0; pPos <= requests.size(); pPos++) {
			for (int dPos = pPos + 1; dPos <= requests.size() + 1; dPos++) {
				
				if (vehicle.isInsertionPossible(pickup, pPos, dPos)) {
					
					vehicle.insertRequest(pickup, pPos, dPos);
					newObjective = objective.calculateForRoute(route);
					
					if (newObjective < minObjective) {
						minObjective = newObjective;
						pickupPosition = pPos;
						deliveryPosition = dPos;
					}
					
					vehicle.removeRequest(pickup);
				}
			}
		}
		
		if (pickupPosition > Integer.MIN_VALUE
				&& deliveryPosition > Integer.MIN_VALUE) {
			
			vehicle.insertRequest(pickup, pickupPosition, deliveryPosition);
		}
		
		return pickupPosition;
	}

}
