package pl.edu.agh.io.pdptw.test.util;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;

public class DataGenerator {
	public static List<Request> generateRequestsPool(int n) {
		List<Request> result = new ArrayList<>();
		
		int timeWindowWidth = 100;
		int curEndTime = timeWindowWidth;
		int curStartTime = 0;
		int serviceTime = 50;
		int xDiff = 1;
		int yDiff = 0;
		int curX = 0;
		int curY = 0;
		int volume = 90;
		Request prevRequest = null;
		
		for (int i = 0; i < n; i++) {
			Location location = new Location(curX, curY);
			Request request = null;
			
			if (i % 2 == 0) {
				request = new PickupRequest(
						i + 1, location, volume, curStartTime, curEndTime, serviceTime);
			} else {
				request = new DeliveryRequest(
						i + 1, location, -volume, curStartTime, curEndTime, serviceTime);
				prevRequest.setSibling(request);
				request.setSibling(prevRequest);
			}
			
			result.add(request);
			curX += xDiff;
			curStartTime = curEndTime + serviceTime;
			curEndTime = curStartTime + timeWindowWidth;
			prevRequest = request;
		}
		
		return result;
	}
	
	public static Route generateRoute(int n) {
		return new Route(generateRequestsPool(n));
	}
}
