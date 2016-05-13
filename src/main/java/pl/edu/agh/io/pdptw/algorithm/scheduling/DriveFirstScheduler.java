package pl.edu.agh.io.pdptw.algorithm.scheduling;

import java.util.Iterator;
import java.util.List;

import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class DriveFirstScheduler implements Scheduler {

	@Override
	public void scheduleRequests(Vehicle vehicle) {
		List<Request> requests = vehicle.getRoute().getRequests();
		
		if (requests.size() > 1) {
			Iterator<Request> it = requests.iterator();
			Request prev = it.next();
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
				prev = cur;
			}
		}
	}

	@Override
	public void updateSuccessor(Request prev, Request cur) {
		double distance = Location.calculateDistance(prev.getLocation(), cur.getLocation());
		cur.setRealizationTime((int) (prev.getRealizationTime()
				+ prev.getServiceTime()
				+ distance));
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
}
