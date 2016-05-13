package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Data
public class Request {
	
	/* Time window parameters explanation:
	 * @timeWindowStart - earliest possible request realization time
	 * @timeWindowEnd - latest possible request realization time
	 * 
	 * ATTENTION! It's the latest possible time of starting request realization,
	 * It is NOT THE LATEST ALLOWED TIME OF DEPARTURE from the service location
	 *  
	 * @serviceTime - time required to serve the specific request
	 * @arrivalTime - time of the arrival of the request to the requests pool */
	
    @Getter @Setter protected Integer id;
    @Getter @Setter protected Location location;
    @Getter @Setter protected Integer volume;
    @Getter @Setter protected Integer timeWindowStart;
    @Getter @Setter protected Integer timeWindowEnd;
    @Getter @Setter protected Integer serviceTime;
    @Getter @Setter protected Integer realizationTime;
    @Getter @Setter protected Integer arrivalTime;
    @Getter protected Request sibling;
    
    /* It is necessary not to make setter method
    for TYPE member available from this abstract 
    class. We want to prevent users from assigning
    arbitrary request type for any request object
    e.g. assigning DeliveryRequest type to an object 
    of class PickupRequest.
    Storing this field will be helpful while implementing
    algorithms operating on objects of this class. */
    
    @Getter protected RequestType type;
    
	public Request(Integer id, Location location, Integer volume,
			Integer timeWindowStart, Integer timeWindowEnd,
			Integer serviceTime, RequestType type) {
		
		super();
		this.id = id;
		this.location = location;
		this.volume = volume;
		this.timeWindowStart = timeWindowStart;
		this.timeWindowEnd = timeWindowEnd;
		this.serviceTime = serviceTime;
		
		/* default arrival time value
		 * for the static problem
		 * if necessary it can be updated */
		
		this.arrivalTime = 0;
		this.realizationTime = timeWindowStart;
		this.type = type;
	}
	
	public void setSibling(Request request) throws IllegalArgumentException {
		if (request.getType() == type
			|| !request.getVolume().equals(-volume)) {
			
			throw new IllegalArgumentException("Assigning sibling of the same type "
					+ " or with different volume is not allowed");
		}
		
		this.sibling = request;
	}
	
	@Override
	public String toString() {
		String desc = "id: " + id
				+ ", location: " + location
				+ ", earliest request realization: " + timeWindowStart
				+ ", latest request realization: " + timeWindowEnd
				+ ", latest departure: " + (timeWindowEnd + serviceTime)
				+ ", service time: " + serviceTime
				+ ",\n planned realization time: " + realizationTime
				+ ", arrival time: " + arrivalTime
				+ ", type: " + type.toString()
				+ ", sibling id: " + ((sibling != null) ? sibling.getId() : "N/A")
				+ ", volume: " + volume;
				
		return desc;
	}
}
