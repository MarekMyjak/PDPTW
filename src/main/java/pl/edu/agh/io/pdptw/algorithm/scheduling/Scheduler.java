package pl.edu.agh.io.pdptw.algorithm.scheduling;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface Scheduler {
	
	void scheduleRequests(Vehicle vehicle, int firstEarliestRealizationTime);
	void updateSuccessor(Request prev, Request cur);
	void updateRequestRealizationTime(Request req, int time);
	int getSuccessorRealizationTime(Request prev, Request cur);
}