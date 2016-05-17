package pl.edu.agh.io.pdptw.algorithm.removal;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.Pair;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class WorstRemoval implements RemovalAlgorithm {

	@Override
	public Request removeRequestForVehicle(Vehicle vehicle, Objective objective) {
		Pair<Integer, Integer> worstPositions = findBestRemovalPositions(vehicle, objective);
		Request pickup = vehicle.getRoute()
				.getRequests()
				.get(worstPositions.getFirst());
		vehicle.removeRequest(worstPositions.getFirst(), worstPositions.getSecond());
		return pickup;
	}

	@Override
	public Pair<Integer, Integer> findBestRemovalPositions(Vehicle vehicle,
			Objective objective) {
		
		double minObjective = Integer.MAX_VALUE;
		double newObjective = Integer.MAX_VALUE;
		Pair<Integer, Integer> worstPositions = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Route route = vehicle.getRoute();
		List<PickupRequest> pickupRequests = (List<PickupRequest>)route.getRequests().stream()
				.filter(r -> r.getType() == RequestType.PICKUP)
				.map(r -> (PickupRequest) r)
				.collect(Collectors.toList());
		
		/* looking for the requests pair
		 * such that after its removal
		 * the new objective value is least */
		
		for (PickupRequest pickup : pickupRequests) {
			Pair<Integer, Integer> positions = vehicle.removeRequest(pickup);
			newObjective = objective.calculateForVehicle(vehicle);
			vehicle.insertRequest(pickup, positions.getFirst(), positions.getSecond());
			
			if (newObjective < minObjective) {
				worstPositions = positions;
			}
		}
		
		return worstPositions;
	}

	@Override
	public Request removeRequestFromSolution(Solution solution,
			Objective objective) {
		// TODO Auto-generated method stub
		return null;
	}

}
