package pl.edu.agh.io.pdptw.model;

public class PickupRequest extends Request {

	public PickupRequest(int id, Location location, int volume,
			int timeWindowStart, int timeWindowEnd,
			int serviceTime) {
		
		super(id, location, volume, timeWindowStart,
				timeWindowEnd, serviceTime, RequestType.PICKUP);
	}
}
