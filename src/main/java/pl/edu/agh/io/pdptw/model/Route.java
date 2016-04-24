package pl.edu.agh.io.pdptw.model;

import java.util.HashSet;
import java.util.Set;

public class Route {
	private final Set<Request> servicedRequests;
	private final Set<Request> requests;
	
	public Route(Set<Request> requests) {
		super();
		this.servicedRequests = new HashSet<>();
		this.requests = requests;
	}
	
	public void markRequestAsServiced(Request request) throws IllegalArgumentException {
		if (!requests.contains(request)) {
			throw new IllegalArgumentException("No such request found");
		}
		
		requests.remove(request);
		servicedRequests.add(request);
	}
	
	public void insertRequestAt(Request request, int position) {
		
	}

	public Set<Request> getServicedRequests() {
		return servicedRequests;
	}

	public Set<Request> getRequests() {
		return requests;
	}
}
