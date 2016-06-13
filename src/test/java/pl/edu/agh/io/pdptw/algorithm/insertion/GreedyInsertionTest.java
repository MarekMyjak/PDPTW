package pl.edu.agh.io.pdptw.algorithm.insertion;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class GreedyInsertionTest {
	private final int REQUESTS_NO = 10;
	private Route route = DataGenerator.generateRoute(REQUESTS_NO);
	private InsertionAlgorithm insertionAlg = new GreedyInsertion();
	
	@Test
	public void test() {
		List<Request> pool = route.getRequests();
		Request lastRequest = pool.get(pool.size() - 1);
		
		PickupRequest pickup = new PickupRequest(
				lastRequest.getId() + 1, new Location(lastRequest.getLocation().getX() + 1, 0),
				100, lastRequest.getTimeWindowEnd() + 20, lastRequest.getTimeWindowEnd() + 100, 50);
		DeliveryRequest delivery = new DeliveryRequest(
				pickup.getId() + 1, new Location(pickup.getLocation().getX() + 1, 0),
				-100, pickup.getTimeWindowEnd() + 20, pickup.getTimeWindowEnd() + 100, 50);
		
		pickup.setSibling(delivery);
		delivery.setSibling(pickup);
		Vehicle vehicle = new Vehicle("test_truck", 200, new Location(0, 0));
		vehicle.setRoute(route);
		Vehicle.setScheduler(new DriveFirstScheduler());
		Configuration configuration = DataGenerator.generateConfiguration();
		
		boolean expected = true;
		boolean actual = insertionAlg.insertRequestForVehicle(pickup, vehicle, configuration);
		assertEquals(expected, actual);
	}

}
