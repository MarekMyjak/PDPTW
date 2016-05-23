package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(exclude={"sibling"})
@Data

/* ATTENTION: 
 * Using lombok's @ToString annotation causes
 * StackOverflowException because
 * a Request object contains other 
 * object of this class so we deal
 * with a cyclical toString() calls. */

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
	
	protected Integer id;
	protected Location location;
	protected Integer volume;
	protected Integer timeWindowStart;
	protected Integer timeWindowEnd;
	protected Integer serviceTime;
	protected Integer realizationTime;
	protected Integer arrivalTime;
	protected Request sibling;
    
    /* It is necessary not to make setter method
    for TYPE member available from this abstract 
    class. We want to prevent users from assigning
    arbitrary request type for any request object
    e.g. assigning DeliveryRequest type to an object 
    of class PickupRequest.
    Storing this field will be helpful while implementing
    algorithms operating on objects of this class. */
    
    protected RequestType type;
    
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
		return String.format("id: %d, type: %s, loc: %s, tstart: %d, realt: %d, tend: %d, servt: %d,"
				+ " sid: %s, v: %d", 
				id, type.toString(), location, timeWindowStart, realizationTime, timeWindowEnd, serviceTime,
				(sibling != null) ? "" + sibling.getId() : "NO SIBLING", volume);
	}
}
