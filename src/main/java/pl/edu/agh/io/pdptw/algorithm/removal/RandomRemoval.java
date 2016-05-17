package pl.edu.agh.io.pdptw.algorithm.removal;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.Pair;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class RandomRemoval implements RemovalAlgorithm {

	@Override
	public Request removeRequestForVehicle(Vehicle vehicle, Objective objective) {
		Route route = vehicle.getRoute();
		int randomPosition = (int) (Math.random() * route.getRequests().size());
		return vehicle.removeRequest(randomPosition);
	}

	@Override
	public Pair<Integer, Integer> findBestRemovalPositions(Vehicle vehicle,
			Objective objective) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Request removeRequestFromSolution(Solution solution,
			Objective objective) {
		// TODO Auto-generated method stub
		return null;
	}

}
