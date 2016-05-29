package pl.edu.agh.io.pdptw.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.io.pdptw.algorithm.scheduling.Scheduler;

@Data
@AllArgsConstructor

public class Vehicle {
    private final String id;
    private final Integer maxCapacity;
    private Integer currentlyLoaded;
    private Location location;
    private final Location startLocation;
    private Route route;
    
    /* note the static keyword */
    @Setter @Getter private static Scheduler scheduler;
    
	public Vehicle(String id, Integer maxCapacity,
			Location startLocation) {
		
		this(id, maxCapacity, 0, startLocation, startLocation, 
				new Route(new ArrayList<Request>()));
	}
	
	public boolean isInsertionPossible(PickupRequest pickupRequest, int pickupPosition, int deliveryPosition) {
		List<Request> requests = route.getRequests();
		
		assert pickupPosition <= route.getRequests().size();
		assert deliveryPosition <= route.getRequests().size();
		assert pickupPosition < deliveryPosition;
		assert pickupPosition <= requests.size();
		assert deliveryPosition <= requests.size() + 1;
		
		/* Check whether:
		 * - the total volume of packages
		 * is less or equal to the maximum capacity of
		 * the vehicle. 
		 * - updated realization times satisfy the 
		 * time window constraints (realizationTime <= timeWindowEnd)
		 */

		Request deliveryRequest = pickupRequest.getSibling();
		boolean insertionPossible = true;
		Request prev = null;
		Request cur = null;
		double totalVolume = 0;
		Iterator<Request> it = requests.iterator();
		int prevOriginalRealizationTime = 0;
		int curOriginalRealizationTime = 0;
		int counter = 0;
		
		while (insertionPossible
				&& it.hasNext() 
				&& counter < pickupPosition) {
			
			prev = it.next();
			totalVolume += prev.getVolume();
			insertionPossible = (totalVolume <= maxCapacity);
			counter++;
		}
		
		if (prev != null) {
			scheduler.updateSuccessor(prev, pickupRequest);
			insertionPossible = insertionPossible
					&& (pickupRequest.getRealizationTime() <= pickupRequest.getTimeWindowEnd());
					
		}
		
		totalVolume += pickupRequest.getVolume();
		insertionPossible = insertionPossible && (totalVolume <= maxCapacity);
		prev = pickupRequest;
		prevOriginalRealizationTime = pickupRequest.getRealizationTime();
		counter++;
		
		while (insertionPossible 
				&& it.hasNext() 
				&& counter < deliveryPosition) {
			
			cur = it.next();
			totalVolume += cur.getVolume();
			curOriginalRealizationTime = cur.getRealizationTime();
			scheduler.updateSuccessor(prev, cur);
			insertionPossible = (cur.getRealizationTime() <= cur.getTimeWindowEnd())
					&& (totalVolume <= maxCapacity);
			prev.setRealizationTime(prevOriginalRealizationTime);
			prev = cur;
			prevOriginalRealizationTime = curOriginalRealizationTime;
			counter++;
		}
		
		if (prev != null) {
			scheduler.updateSuccessor(prev, deliveryRequest);
			prev.setRealizationTime(prevOriginalRealizationTime);
			insertionPossible = insertionPossible 
					&& (deliveryRequest.getRealizationTime() <= deliveryRequest.getTimeWindowEnd());
		}
		
		if (insertionPossible) {
			totalVolume += deliveryRequest.getVolume();
			insertionPossible = insertionPossible && (totalVolume <= maxCapacity);
			prev = deliveryRequest;
			prevOriginalRealizationTime = deliveryRequest.getRealizationTime();
			counter++;
		}
		
		while (insertionPossible 
				&& it.hasNext()) {
			
			cur = it.next();
			totalVolume += cur.getVolume();
			curOriginalRealizationTime = cur.getRealizationTime();
			scheduler.updateSuccessor(prev, cur);
			insertionPossible = (cur.getRealizationTime() <= cur.getTimeWindowEnd())
					&& (totalVolume <= maxCapacity);
			prev.setRealizationTime(prevOriginalRealizationTime);
			prev = cur;
			prevOriginalRealizationTime = curOriginalRealizationTime;
			counter++;
		}
		
		if (prev != null) {
			prev.setRealizationTime(prevOriginalRealizationTime);
		}
		
		if (!insertionPossible) {
			pickupRequest.setRealizationTime(pickupRequest.getTimeWindowStart());
			deliveryRequest.setRealizationTime(deliveryRequest.getTimeWindowStart());
		}
		
		return insertionPossible;
	}
	
	public void updateRealizationTimes() {
		scheduler.scheduleRequests(this);
	}
	
	public void insertRequest(PickupRequest pickupRequest, int pickupPosition, int deliveryPosition) {
		List<Request> requests = route.getRequests();
		requests.add(pickupPosition, pickupRequest);
		requests.add(deliveryPosition, pickupRequest.getSibling());
		updateRealizationTimes();
	}
	
	public Request removeRequest(int pickupPosition, int deliveryPosition) {
		List<Request> requests = route.getRequests();
		
		assert pickupPosition >= 0;
		assert deliveryPosition >= 0;
		assert pickupPosition < requests.size();
		assert deliveryPosition < requests.size();
		
		Request pickup = requests.remove(pickupPosition);
		
		/* [deliveryPosition -1] because after removing the pickup request
		 * the position of the delivery request is decremented
		 * by 1. Note that pickup request is always added
		 * to the route before the corresponding delivery request. */
		
		requests.remove(deliveryPosition - 1);
		updateRealizationTimes();
		
		return pickup;
	}
	
	public Request removeRequest(int pickupPosition) {
		List<Request> requests = route.getRequests();
		
		assert pickupPosition >= 0;
		assert pickupPosition < requests.size();
		
		Request deliveryRequest = requests.get(pickupPosition).getSibling();
		requests.remove(pickupPosition);
		
		/* [deliveryPosition -1] because after removing the pickup request
		 * the position of the delivery request is decremented
		 * by 1. Note that pickup request is always added
		 * to the route before the corresponding delivery request. */
		
		requests.remove(deliveryRequest);
		updateRealizationTimes();
		
		return deliveryRequest.getSibling();
	}
	
	public RequestPositions removeRequest(PickupRequest pickupRequest) {
		List<Request> requests = route.getRequests();
		
		assert requests.contains(pickupRequest);
		assert requests.contains(pickupRequest.getSibling());
		
		int pickupPosition = requests.indexOf(pickupRequest);
		int deliveryPosition = requests.indexOf(pickupRequest.getSibling());
		requests.remove(pickupRequest);
		requests.remove(pickupRequest.getSibling());
		updateRealizationTimes();
		
		return new RequestPositions(pickupPosition, deliveryPosition);
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
	
	public Vehicle createShallowCopy() {
		Vehicle copy = new Vehicle(id, maxCapacity, startLocation);
		copy.setCurrentlyLoaded(currentlyLoaded);
		copy.setLocation(location);
		copy.setRoute(route);
		
		return copy;
	}
    
}
