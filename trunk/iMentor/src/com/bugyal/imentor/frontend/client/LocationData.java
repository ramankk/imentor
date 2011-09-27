package com.bugyal.imentor.frontend.client;

public class LocationData {

	String location;
	int radius;
	double latitude;
	double longitude;

	public LocationData() {

	}

	public LocationData(String location, double latitude, double longitude,
			int radius) {
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int range) {
		this.radius = range;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
