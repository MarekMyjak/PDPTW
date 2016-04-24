package pl.edu.agh.io.pdptw.model;

import java.util.HashSet;
import java.util.Set;

public class Solution {
	private final Set<Vehicle> vehicles;
	
	public Solution(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
	public Set<Route> getRoutes() {
		Set<Route> result = new HashSet<>();
		
		for (Vehicle vehicle : vehicles) {
			result.add(vehicle.getRoute());
		}
		
		return result;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}
}
