package pl.edu.agh.io.pdptw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Route {
	private final List<Request> servicedRequests;
	private final List<Request> requests;
	
	public Route(List<Request> requests) {
		super();

		this.servicedRequests = new ArrayList<>();
		this.requests = requests;
	}
	
	public void markRequestAsServiced(Request request) throws IllegalArgumentException {
		if (!requests.contains(request)) {
			throw new IllegalArgumentException("No such request found");
		}
		
		requests.remove(request);
		servicedRequests.add(request);
	}
}
