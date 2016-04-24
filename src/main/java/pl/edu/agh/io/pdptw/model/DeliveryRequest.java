package pl.edu.agh.io.pdptw.model;

public class DeliveryRequest extends Request {

	public DeliveryRequest(Location location, Integer volume,
			Integer timeWindowStart, Integer timeWindowEnd,
			Integer serviceTime) {
		
		super(location, volume, timeWindowStart, timeWindowEnd, serviceTime, RequestType.DELIVERY);
	}
	
	public void setSibling(PickupRequest request) {
		this.sibling = (Request) request;
	}
}
