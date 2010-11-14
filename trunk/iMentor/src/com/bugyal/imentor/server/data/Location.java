package com.bugyal.imentor.server.data;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
@EmbeddedOnly
public class Location {
	@Persistent
	double latitude;

	@Persistent
	double longitude;
	
	@Persistent
	String locationString;

	@Persistent
	int activeRadius; // in kilo-meters.

	public Location(Location copy) {
		this.latitude = copy.getLatitude();
		this.longitude = copy.getLongitude();
		this.locationString = copy.getLocationString();
		this.activeRadius = copy.getActiveRadius();
	}
	
	public Location(double latitude, double longitude, String locationString,
			int activeRadius) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationString = locationString;
		this.activeRadius = activeRadius;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLocationString() {
		return locationString;
	}

	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	public int getActiveRadius() {
		return activeRadius;
	}

	public void setActiveRadius(int activeRadius) {
		this.activeRadius = activeRadius;
	}
}
