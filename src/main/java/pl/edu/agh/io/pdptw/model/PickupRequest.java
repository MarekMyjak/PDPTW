package pl.edu.agh.io.pdptw.model;

public class PickupRequest extends Request {

	public PickupRequest(int id, Location location, int volume,
			int timeWindowStart, int timeWindowEnd,
			int serviceTime) {
		
		super(id, location, volume, timeWindowStart,
				timeWindowEnd, serviceTime, RequestType.PICKUP);
	}
	
	@Override
	public Request createShallowCopy() {
		Request copy =  new PickupRequest(id, location, volume, timeWindowStart, timeWindowEnd, serviceTime);
		copy.setRealizationTime(realizationTime);
		copy.setArrivalTime(arrivalTime);
		copy.setSibling(sibling);
		return copy;
	}
}
