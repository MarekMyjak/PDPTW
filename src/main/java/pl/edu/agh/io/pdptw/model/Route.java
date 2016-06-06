package pl.edu.agh.io.pdptw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode

public class Route {
	private final List<Request> requests;
	
	public Route(List<Request> requests) {
		super();

		this.requests = requests;
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
	
	public Route copy() {
		Map<Integer, Request> requestsForIds = new HashMap<>();
		List<Request> requestsCopies = requests.stream()
				.map(r -> r.createShallowCopy())
				.collect(Collectors.toList());
		
		requestsCopies.stream()
				.filter(r -> r.getType() == RequestType.PICKUP)
				.collect(Collectors.toList())
				.forEach(p -> requestsForIds.put(p.getId(), p));
		
		requestsCopies.stream()
				.filter(r -> r.getType() == RequestType.DELIVERY)
				.collect(Collectors.toList())
				.forEach(d -> {
					Request p = requestsForIds.get(d.getSibling().getId());
					d.setSibling(p);
					p.setSibling(d);
				});
		return new Route(requestsCopies);
	}
}
