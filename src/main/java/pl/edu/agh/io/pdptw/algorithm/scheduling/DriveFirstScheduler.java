package pl.edu.agh.io.pdptw.algorithm.scheduling;

import java.util.Iterator;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class DriveFirstScheduler implements Scheduler {

	@Override
	public void scheduleRequests(Vehicle vehicle, int firstEarliestRealizationTime) 
		throws IllegalArgumentException {
		
		Vehicle copy = vehicle.copy();
		
		if (vehicle.getRoute().getRequests().size() > 0) {
			
			/* skip all the requests whose end of the time window
			 * is earlier than the firstEarliestRealizationTime
			 * 
			 *  it's needed because while removing finished
			 *  requests from a vehicle we leave there pickup
			 *  requests which have a corresponding delivery request
			 *  that is yet to be served */
			
			Iterator<Request> it = vehicle.getRoute().getRequests()
					.stream()
					.filter(r -> r.getTimeWindowEnd() > firstEarliestRealizationTime)
					.collect(Collectors.toList())
					.iterator();
			
			Request prev = it.next();
			
			/* we need to check if the earliest realization time
			 * is earlier than the end of the time window of the
			 * first request */
			
			prev.setRealizationTime((firstEarliestRealizationTime >= prev.getTimeWindowStart())
					? firstEarliestRealizationTime 
					: prev.getTimeWindowStart());
			prev.setRealizationTime(prev.getTimeWindowStart());
			
			while (it.hasNext()) {
				Request cur = it.next();
				double distance = Location.calculateDistance(prev.getLocation(), cur.getLocation());
				int earliestRealizationTime = (int) (prev.getRealizationTime() 
						+ prev.getServiceTime() 
						+ distance);
				int timeWindowStart = cur.getTimeWindowStart();
				
				cur.setRealizationTime((earliestRealizationTime >= timeWindowStart)
						? earliestRealizationTime 
						: timeWindowStart);
				
				/* we need to check if the earliest realization time
				 * is earlier than the end of the time window */
				
				if (earliestRealizationTime > cur.getTimeWindowEnd()) {
					System.out.println(copy);
					System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^");
					System.out.println(vehicle);
					throw new IllegalArgumentException("Earliest realization time"
							+ " is greater than the end of the time window" + earliestRealizationTime);
				}
				
				prev = cur;
			}
		}
	}

	@Override
	public void updateSuccessor(Request prev, Request cur) {
		double distance = Location.calculateDistance(prev.getLocation(), cur.getLocation());
		int earliestRealizationTime = (int) (prev.getRealizationTime()
				+ prev.getServiceTime()
				+ distance);
		
		earliestRealizationTime = (earliestRealizationTime > cur.getTimeWindowStart())
				? earliestRealizationTime
				: cur.getTimeWindowStart();
		
		cur.setRealizationTime(earliestRealizationTime);
	}

	@Override
	public int getSuccessorRealizationTime(Request prev, Request cur) {
		int result = 0;
		int distance = (int) Location.calculateDistance(prev.getLocation(), cur.getLocation());
		int earliestRealizationTime = prev.getRealizationTime() + prev.getServiceTime() + distance;
		
		result = (earliestRealizationTime > cur.getTimeWindowStart()) 
				? earliestRealizationTime
				: cur.getTimeWindowStart();
						
		return result;
	}

	/* note that we don't guarantee here
	 * that after updating the realization time
	 * it will satisfy the time window constraint
	 * (it may be bigger than the end of the time window!)
	 * because it is meant to be used to check update
	 * the pickup request  */
	
	@Override
	public void updateRequestRealizationTime(Request req, int time) {
		
		/* if the new realization time  */
		
		if (req.getRealizationTime() < time) {
			req.setRealizationTime(time);
		}
	}
}
