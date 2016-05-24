package pl.edu.agh.io.pdptw.algorithm.generation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class SweepGeneration implements GenerationAlgorithm {

	@Override
	public Solution generateSolution(List<Request> requestPool, List<Vehicle> vehicles,
			Configuration config)
			throws IllegalArgumentException {
		
		if (requestPool == null) {
			throw new IllegalArgumentException("Pool of requests list is set to NULL");
		}
		if (vehicles == null) {
			throw new IllegalArgumentException("Vehicles list is set to NULL");
		}
		
		InsertionAlgorithm insertionAlg = config.getAlgorithms().getInsertionAlgorithm();
		Objective objective = config.getAlgorithms().getObjective();
		List<Vehicle> usedVehicles = new LinkedList<>();
		Solution result = new Solution(usedVehicles);
		Location warehouseLocation = vehicles.get(0).getLocation();

		/* calculate the polar angle between the warehouse (the start point)
		 * and each request location */
		
		requestPool.forEach(r -> r.getLocation().updatePolarAngle(warehouseLocation));
		
		/* sort requests by the increasing polar angle */
		
		List<PickupRequest> pickupRequests = requestPool.stream()
				.filter(r -> r.getType() == RequestType.PICKUP)
				.map(r -> (PickupRequest) r)
				.sorted((r1, r2) -> {
					double r1Angle = r1.getLocation().getPolarAngle();
					double r2Angle = r2.getLocation().getPolarAngle();
					
					return (r1Angle < r2Angle) 
							? -1 
							: (r1Angle > r2Angle) ? 1 : 0;
				}).collect(Collectors.toCollection(LinkedList::new));
		
		Iterator<Vehicle> vehiclesIt = vehicles.iterator();
		boolean insertedSuccessfully = true;
		
		while (pickupRequests.size() > 0
				&& vehiclesIt.hasNext()) {
			
			PickupRequest curRequest = pickupRequests.remove(0); 
			Vehicle curVehicle = vehiclesIt.next();
			insertedSuccessfully = insertionAlg.insertRequestForVehicle(curRequest, curVehicle, objective);
			Location pickupLocation = curRequest.getLocation();
			Location deliveryLocation = curRequest.getSibling().getLocation();
			
			Location lowerBoundary = (pickupLocation.getPolarAngle() 
					<= deliveryLocation.getPolarAngle())
						? pickupLocation 
						: deliveryLocation;
			Location upperBoundary = (lowerBoundary == pickupLocation) 
					? deliveryLocation
					: pickupLocation;
			
			/* select requests in the sector marked out by the location
			 *  of the current pickup and delivery requests 
			 *  i.e. lying in the area within the 
			 *  lower boundary - warehouse - upper boundary angle */
			
			List<PickupRequest> pickupsInSector = pickupRequests.stream()
					.filter(r -> {
						Location p = r.getLocation();
						Location d = r.getSibling().getLocation();
						
						return p.getPolarAngle() >= lowerBoundary.getPolarAngle()
							&& p.getPolarAngle() <= upperBoundary.getPolarAngle()
							&& d.getPolarAngle() >= lowerBoundary.getPolarAngle()
							&& d.getPolarAngle() <= lowerBoundary.getPolarAngle();
							
 					}).collect(Collectors.toList());
			Iterator<PickupRequest> sectorIt = pickupsInSector.iterator();
			
			/* Try to insert each request */
			
			while (sectorIt.hasNext()) {
				PickupRequest pickupToInsert = sectorIt.next();
				if (pickupToInsert.getId().equals(56)) {
					System.out.println("sector 56");
				}
				insertedSuccessfully = insertionAlg.insertRequestForVehicle(
						pickupToInsert, curVehicle, objective);
				
				/* Remove the newly dispatched request from the 
				 * general and sector pools */
				
				if (insertedSuccessfully) {
					pickupRequests.remove(pickupToInsert);
					sectorIt.remove();
				}
			}
			
			/* We've inserted all requests in the current
			 * sector. Now we should check whether we can
			 * add some more requests. In order to do this
			 * we'll sort the remaining pickup requests by
			 * the angle between them and the upper boundary. */
			
			if (pickupsInSector.size() == 0
					&& insertedSuccessfully) {
				
				double curAngle = curRequest.getLocation().getPolarAngle();
				Iterator<PickupRequest> nearestIt = pickupRequests.stream()
						.sorted((r1, r2) -> {
							Location p1 = r1.getLocation();
							Location p2 = r2.getLocation();
							double diff1 = Math.abs(p1.getPolarAngle() - curAngle);
							double diff2 = Math.abs(p2.getPolarAngle() - curAngle);
							
							return (diff1 < diff2)
									? -1
									: (diff1 > diff2) ? 1 : 0;
									
						})
						.collect(Collectors.toList())
						.iterator();
				
				while (insertedSuccessfully && nearestIt.hasNext()) {
					PickupRequest nearestRequest = nearestIt.next();
					insertedSuccessfully = insertionAlg.insertRequestForVehicle(
							nearestRequest, curVehicle, objective);
					if (insertedSuccessfully) {
						pickupRequests.remove(nearestRequest);
					}
				}
			}
			
			usedVehicles.add(curVehicle);
		}
		
		System.out.println();
		
		return result;
	}
}
