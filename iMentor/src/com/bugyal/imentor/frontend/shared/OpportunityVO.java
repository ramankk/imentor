package com.bugyal.imentor.frontend.shared;

import java.io.Serializable;
import java.util.List;

public class OpportunityVO implements Serializable {

	private Long id;
	private List<String> subjects;
	private int requiredMentors;
	private int priority;
	private double latitude;
	private double longitude;
	private int radius;
	private String locString;
	
	// Required by GWT.
	public OpportunityVO() {
		
	}
	
	public OpportunityVO(Long id, List<String> subjects, int requiredMentors,
			int priority, double latitude, double longitude, int radius,
			String locString) {
		super();
		this.id = id;
		this.subjects = subjects;
		this.requiredMentors = requiredMentors;
		this.priority = priority;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.locString = locString;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public int getRequiredMentors() {
		return requiredMentors;
	}

	public void setRequiredMentors(int requiredMentors) {
		this.requiredMentors = requiredMentors;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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

	public String getLocString() {
		return locString;
	}

	public void setLocString(String locString) {
		this.locString = locString;
	}
}
