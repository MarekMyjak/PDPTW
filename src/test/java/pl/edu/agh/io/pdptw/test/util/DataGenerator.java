package pl.edu.agh.io.pdptw.test.util;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class DataGenerator {
	public static List<Request> generateRequestsPool(int requestsNo) {
		List<Request> result = new ArrayList<>();
		
		/* number of siblings must be divisible by 2;
		 * requests are paired (pickup - delivery) */
		
		int n = requestsNo + (requestsNo % 2);
		int timeWindowWidth = 100;
		int curEndTime = timeWindowWidth;
		int curStartTime = 0;
		int serviceTime = 50;
		int xDiff = 5;
		int yDiff = 5;
		int curX = 0;
		int curY = 0;
		int volume = 90;
		Request prevRequest = null;
		
		for (int i = 0; i < n; i++) {
			Location location = new Location(curX, curY);
			Request request = null;
			
			if (i % 2 == 0) {
				request = new PickupRequest(
						(int) (Math.random() * 1000), location, volume, curStartTime, curEndTime, serviceTime);
			} else {
				request = new DeliveryRequest(
						prevRequest.getId() + 1, location, -volume, curStartTime, curEndTime, serviceTime);
				prevRequest.setSibling(request);
				request.setSibling(prevRequest);
			}
			
			result.add(request);
			curX += (int) (Math.random() * 2 * xDiff);
			curY += (int) (Math.random() * 2 * yDiff);
			curStartTime = curEndTime + serviceTime;
			curEndTime = curStartTime + timeWindowWidth;
			prevRequest = request;
		}
		
		return result;
	}
	
	public static Route generateRoute(int n) {
		return new Route(generateRequestsPool(n));
	}
	
	public static Vehicle generateVehicle(int n) {
		Vehicle v = new Vehicle("genericTruck" + ((int) (Math.random() * 1000)), 200, new Location(0, 0));
		v.setRoute(generateRoute(n));
		
		return v;
	}
	
	public static Solution generateSolution(int n) {
		List<Vehicle> vehicles = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			vehicles.add(generateVehicle(5));
		}
		
		return new Solution(vehicles);
	}
	
	public static Configuration generateConfiguration() {
		return new Configuration("", "", "",
				false, 1000, 3, 3, 5,
				new Location(0, 0),
				AlgorithmConfiguration.createDefault());
	}
}
