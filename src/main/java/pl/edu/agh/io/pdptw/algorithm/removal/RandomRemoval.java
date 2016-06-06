package pl.edu.agh.io.pdptw.algorithm.removal;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.optimization.ListUtils;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class RandomRemoval implements RemovalAlgorithm {

	@Override
	public RequestPositions findBestRemovalPositions(Vehicle vehicle,
			Configuration configuration) {
		
		List<Request> requests = vehicle.getRoute().getRequests();
		List<PickupRequest> pickupRequests = requests
				.stream()
				.filter(r -> r.getType() == RequestType.PICKUP
						&& !vehicle.getServedRequestsIds().contains(r.getId()))
				.map(r -> (PickupRequest) r)
				.collect(Collectors.toList());
		PickupRequest pickup = ListUtils.getRandomElement(pickupRequests);
		
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
		Vehicle randomVehicle = ListUtils.getRandomElement(vehicles);
		
		return removeRequestForVehicle(randomVehicle, configuration);
	}


}
