package pl.edu.agh.io.pdptw.algorithm.objective;

import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

import java.util.Iterator;

public class TotalDistanceObjective implements Objective {

	@Override
	public double calculate(Solution solution) {
		return solution.getVehicles().stream()
				.mapToDouble(this::calculateForVehicle)
				.sum();
	}

	@Override
	public double calculateForVehicle(Vehicle vehicle) {
		double result = 0;
		Iterator<Request> it = vehicle.getRoute().getRequests().iterator();
		
		if (it.hasNext()) {
			Request prevRequest = it.next();
			
			/* add the distance between the start location 
			 * and the location of the first reguest */
			
			result += Location.calculateDistance(
					prevRequest.getLocation(), vehicle.getStartLocation());
			
			while (it.hasNext()) {
				Request curRequest = it.next();
				result += Location.calculateDistance(
						prevRequest.getLocation(), curRequest.getLocation());
				prevRequest = curRequest;
			}
			
			/* add the distance between the last request location 
			 * and the vehicle's start location */
			
			result += Location.calculateDistance(
					prevRequest.getLocation(), vehicle.getStartLocation());
			
		}
		
		return result;
	}

}
