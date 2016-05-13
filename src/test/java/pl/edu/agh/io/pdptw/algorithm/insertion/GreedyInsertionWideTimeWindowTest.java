package pl.edu.agh.io.pdptw.algorithm.insertion;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.objective.TotalDistanceObjective;
import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class GreedyInsertionWideTimeWindowTest {
	private final int REQUESTS_NO = 10;
	private Route route = DataGenerator.generateRoute(REQUESTS_NO);
	private Objective objective = new TotalDistanceObjective();
	private InsertionAlgorithm insertionAlg = new GreedyInsertion();
		
	@Test
	public void test() {
		List<Request> pool = route.getRequests();
		Request lastRequest = pool.get(pool.size() - 1);
		
		PickupRequest pickup = new PickupRequest(
				lastRequest.getId() + 1, new Location(0, 0),
				1, 0, 5000, 1);
		DeliveryRequest delivery = new DeliveryRequest(
				pickup.getId() + 1, new Location(pickup.getLocation().getX() + 1, 0),
				-1, 0, 10000, 400);
		
		pickup.setSibling(delivery);
		delivery.setSibling(pickup);
		Vehicle vehicle = new Vehicle("test_truck", 200, new Location(0, 0));
		vehicle.setRoute(route);
		Vehicle.setScheduler(new DriveFirstScheduler());
		
		int expected = 0;
		int actual = insertionAlg.insertRequestToVehicleRoute(pickup, vehicle, objective);
		System.out.println(vehicle);
		Assert.assertEquals(expected, actual, 0);
	}
}
