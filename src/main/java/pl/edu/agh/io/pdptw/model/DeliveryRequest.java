package pl.edu.agh.io.pdptw.model;

public class DeliveryRequest extends Request {

	public DeliveryRequest(Integer id, Location location, Integer volume,
			Integer timeWindowStart, Integer timeWindowEnd,
			Integer serviceTime) {
		
		super(id, location, volume, timeWindowStart,
				timeWindowEnd, serviceTime, RequestType.DELIVERY);
	}
}
