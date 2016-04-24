package pl.edu.agh.io.pdptw.model;


public abstract class Request {
    protected final Location location;
    protected final Integer volume;
    protected Integer timeWindowStart;
    protected Integer timeWindowEnd;
    protected Integer serviceTime;
    protected Request sibling;
    protected final RequestType type;
    
	protected Request(Location location, Integer volume, Integer timeWindowStart,
			Integer timeWindowEnd, Integer serviceTime, RequestType type) {
		
		super();
		this.location = location;
		this.volume = volume;
		this.timeWindowStart = timeWindowStart;
		this.timeWindowEnd = timeWindowEnd;
		this.serviceTime = serviceTime;
		this.type = type;
	}

	public Integer getTimeWindowStart() {
		return timeWindowStart;
	}

	public void setTimeWindowStart(Integer timeWindowStart) {
		this.timeWindowStart = timeWindowStart;
	}

	public Integer getTimeWindowEnd() {
		return timeWindowEnd;
	}

	public void setTimeWindowEnd(Integer timeWindowEnd) {
		this.timeWindowEnd = timeWindowEnd;
	}

	public Integer getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Integer serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Request getSibling() {
		return sibling;
	}

	public Location getLocation() {
		return location;
	}

	public Integer getVolume() {
		return volume;
	}
}
