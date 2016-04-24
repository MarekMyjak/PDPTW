package pl.edu.agh.io.pdptw.model;

public class PickupRequest extends Request {

	public PickupRequest(Location location, Integer volume,
			Integer timeWindowStart, Integer timeWindowEnd,
			Integer serviceTime) {
		
		super(location, volume, timeWindowStart, timeWindowEnd, serviceTime, RequestType.PICKUP);
	}
	
	public void setSibling(DeliveryRequest request) {
		this.sibling = (Request) request;
	}
}
