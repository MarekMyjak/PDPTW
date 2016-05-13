package pl.edu.agh.io.pdptw.algorithm.scheduling;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface Scheduler {
	public void scheduleRequests(Vehicle vehicle);
	public void updateSuccessor(Request prev, Request cur);
	public int getSuccessorRealizationTime(Request prev, Request cur);
}