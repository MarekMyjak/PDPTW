package pl.edu.agh.io.pdptw.algorithm.removal;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class RandomRemoval implements RemovalAlgorithm {

	@Override
	public RequestPositions findBestRemovalPositions(Vehicle vehicle,
			Configuration configuration) {
		
		Route route = vehicle.getRoute();
		List<Request> requests = route.getRequests();
		List<PickupRequest> pickupRequests = (List<PickupRequest>) requests.stream()
				.filter(r -> r.getType() == RequestType.PICKUP)
				.map(r -> (PickupRequest) r)
				.collect(Collectors.toList());
		PickupRequest pickup = pickupRequests.get((int) (Math.random() * pickupRequests.size()));
		
		return new RequestPositions(requests.indexOf(pickup),
				requests.indexOf(pickup.getSibling()));
	}

	@Override
	public Request removeRequestForVehicle(Vehicle vehicle,
			Configuration configuration) {
		RequestPositions positions = findBestRemovalPositions(vehicle, configuration);
		return vehicle.removeRequest(
				positions.getPickupPosition(), positions.getDeliveryPosition());
	}

	@Override
	public Request removeRequestFromSolution(Solution solution,
			Configuration configuration) {
		
		List<Vehicle> vehicles = solution.getVehicles();
		Vehicle randomVehicle = vehicles.get((int) (Math.random() * vehicles.size()));
		
		return removeRequestForVehicle(randomVehicle, configuration);
	}


}
