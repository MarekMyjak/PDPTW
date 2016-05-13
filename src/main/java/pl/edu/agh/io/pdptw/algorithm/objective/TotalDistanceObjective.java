package pl.edu.agh.io.pdptw.algorithm.objective;

import java.util.Iterator;

import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;

public class TotalDistanceObjective implements Objective {

	@Override
	public double calculate(Solution solution) {
		return solution.getRoutes().stream()
				.mapToDouble(this::calculateForRoute)
				.sum();
	}

	@Override
	public double calculateForRoute(Route route) {
		double result = 0;
		Iterator<Request> it = route.getRequests().iterator();
		
		if (it.hasNext()) {
			Request prevRequest = it.next();
			
			while (it.hasNext()) {
				Request curRequest = it.next();
				Location l1 = prevRequest.getLocation();
				Location l2 = curRequest.getLocation();
				
				double xDiff = l1.getX()
						- l2.getX();
				double yDiff = l1.getY()
						- l2.getY();
				
				result += Math.sqrt(xDiff * xDiff + yDiff * yDiff);
				prevRequest = curRequest;
			}
		}
		
		return result;
	}

}
