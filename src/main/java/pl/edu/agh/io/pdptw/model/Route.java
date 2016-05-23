package pl.edu.agh.io.pdptw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode

public class Route {
	private final List<Request> servicedRequests;
	private final List<Request> requests;
	
	public Route(List<Request> requests) {
		super();

		this.servicedRequests = new ArrayList<>();
		this.requests = requests;
	}
	
	public void markRequestAsServed(Request request) throws IllegalArgumentException {
		if (!requests.contains(request)) {
			throw new IllegalArgumentException("No such request found");
		}
		
		requests.remove(request);
		servicedRequests.add(request);
	}
	
	@Override
	public String toString() {
		return "["+ 
				String.join(", ", 
						requests.stream()
							.map(r -> "id=" + r.getId() + " " + r.getLocation())
							.collect(Collectors.toList()))
				+ "]";
	}
}
