package com.bugyal.imentor.frontend.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParticipantVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7785833027106467978L;
	/**
	 * 
	 */
	private Long id;
	private String name;
	private String gender;
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

	public ParticipantVO(Long id, String name, String gender, String email, double latitude,
			double longitude, String locString, int radius, List<String> has,
			List<String> need) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setLocationString(locString);
		this.radius = radius;

		// This is to convert org.datanucleus.sco.backend.ArrayList to simple
		// java.util.ArrayList which
		// is serializable over wire to be used in the GWT world.
		this.hasSubjects = new ArrayList<String>();
		this.hasSubjects.addAll(has);

		this.needSubjects = new ArrayList<String>();
		this.needSubjects.addAll(need);
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

	public String toString() {
		return "Participant " + id + ", name : " + name;
	}

	// This method trims ParticipantVO object to bare minimum information
	// required in SearchResult.
	// If you need the following cleared information, fix it !!
	public void trim() {
		/*this.needSubjects.clear();
		this.hasSubjects.clear();
		this.locationString = null;
		this.email=null;*/
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

}
