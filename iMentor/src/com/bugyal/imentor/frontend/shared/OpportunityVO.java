package com.bugyal.imentor.frontend.shared;

import java.io.Serializable;
import java.util.ArrayList;
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
	private String message;
	
	// Required by GWT.
	public OpportunityVO() {
		
	}
	
	public OpportunityVO(Long id, List<String> subjects, int requiredMentors,
			int priority, double latitude, double longitude, int radius,
			String locString, String message) {
		super();
		this.id = id;
		
		// This is to convert org.datanucleus.sco.backend.ArrayList to simple java.util.ArrayList which
		// is serializable over wire to be used in the GWT world.
		this.subjects = new ArrayList<String>();
		this.subjects.addAll(subjects);
		
		this.requiredMentors = requiredMentors;
		this.priority = priority;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.locString = locString;
		this.message = message;
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
	

	// This method trims ParticipantVO object to bare minimum information required in SearchResult.
	// If you need the following cleared information, fix it !!
	public void trim() {
		//this.subjects.clear();
		//this.locString = null;
	}
	
	public String getSubjectsAsString() {
		StringBuilder subs = new StringBuilder();
		boolean first = true;
		for (String subject : getSubjects()) {
			if (first) {
				first = false;
			} else {
				subs.append(", ");
			}
			subs.append(subject);
		}
		return subs.toString();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
