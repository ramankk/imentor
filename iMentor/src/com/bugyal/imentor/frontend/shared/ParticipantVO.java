package com.bugyal.imentor.frontend.shared;

import java.io.Serializable;
import java.util.List;

public class ParticipantVO implements Serializable {
	
	private Long id;
	private String name;
	private String email;
	private double latitude;
	private double longitude;
	private int radius;
	private List<String> hasSubjects;
	private List<String> needSubjects;
	private String locationString;
	
	// Required for GWT.
	public ParticipantVO() {
		
	}
	
	public ParticipantVO(Long id, String name, String email, double latitude,
			double longitude, String locString, int radius, List<String> has, List<String> need) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setLocationString(locString);
		this.radius = radius;
		this.hasSubjects = has;
		this.needSubjects = need;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public List<String> getHasSubjects() {
		return hasSubjects;
	}

	public void setHasSubjects(List<String> subjects) {
		this.hasSubjects = subjects;
	}

	public List<String> getNeedSubjects() {
		return needSubjects;
	}

	public void setNeedSubjects(List<String> subjects) {
		this.needSubjects = subjects;
	}

	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	public String getLocationString() {
		return locationString;
	}

	@Override
	public String toString() {
		return "Participant " + id + ", name : " + name;
	}
	
}
