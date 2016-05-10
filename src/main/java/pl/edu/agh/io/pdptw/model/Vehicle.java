package pl.edu.agh.io.pdptw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vehicle {
    private final String id;
    private final Integer maxCapacity;
    private Integer currentlyLoaded;
    private Location location;
    private final Location startLocation;
    private Route route;
    
	public Vehicle(String id, Integer maxCapacity,
			Location startLocation) {
		
		this(id, maxCapacity, 0, startLocation, startLocation, 
				new Route(new ArrayList<Request>()));
	}
	
	public boolean insertRequest(int position, Request request) {
		assert position <= route.getRequests().size();
		
		boolean insertionPossible = true;
		List<Request> pool = route.getRequests();
		Request next = null;
		Request prev = null;
		double prevDistance = 0;
		double nextDistance = 0;
		double xDiff = 0;
		double yDiff = 0;
		
		/* Check whether it's possible to realize
		 * the previous request before starting
		 * serving the one being inserted. */
		
		if (position > 0) {
			prev = pool.get(position - 1);
			xDiff = prev.getLocation().getX()
					- request.getLocation().getX();
			yDiff = prev.getLocation().getY()
					- request.getLocation().getY();
			prevDistance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			
			insertionPossible = (prev.getTimeWindowEnd() 
					+ prev.getServiceTime() 
					+ prevDistance <= request.getTimeWindowEnd());
		} 
		
		/* Check whether it's possible to realize
		 * the currently inserted request before starting
		 * serving the next one. */
		
		if (insertionPossible && position < pool.size()) {
			next = pool.get(position);
			xDiff = request.getLocation().getX()
					- next.getLocation().getX();
			yDiff = request.getLocation().getY()
					- next.getLocation().getY();
			nextDistance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			
			insertionPossible = insertionPossible
					&& (request.getTimeWindowEnd()
						+ request.getServiceTime()
						+ nextDistance <= next.getTimeWindowEnd());
		}
		
		/* Check whether the total volume of packages
		 * is less or equal to the maximum capacity of
		 * the vehicle. 
		 * 
		 * TODO: If we insert a pickup request, how
		 * should we check whether the capacity constraint
		 * is satisfied without knowledge of the position
		 * of the corresponding delivery request?  
		 * */
		
		if (insertionPossible) {
			double loadedVolume = pool.stream()
					.mapToDouble(Request::getVolume)
					.sum();
			if (loadedVolume <= maxCapacity) {
				pool.add(position, request);
			} else {
				insertionPossible = false;
			}
		}
		
		return insertionPossible;
	}
    
	@Override
	public String toString() {
		String representation = String.format(
				"id: %s, max capacity: %d, currently loaded: %d, location: (%d, %d), route: \n[\n",
				id, maxCapacity, currentlyLoaded, location.getX(), location.getY());
		List<String> requestsStrings = route.getRequests().stream()
				.map(Request::toString)
				.collect(Collectors.toList());
		
		for (String req : requestsStrings) {
			representation += "\t" + req + "\n";
		}
		
		representation += "]";
		return representation;
	}
    
}
