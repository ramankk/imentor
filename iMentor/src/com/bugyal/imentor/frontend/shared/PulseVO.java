package com.bugyal.imentor.frontend.shared;

import java.io.Serializable;

public class PulseVO implements Serializable{		
	

	public enum State {
		mentor,
		mentee,
		user
	}
	
	private static final long serialVersionUID = -7381764235562694506L;
	private String emailId;
	private String name;
	private String facebookId;
	private double longitude; 
	private double latitude;
	private String locationString;
	private State state;
	private String othersFacebookId;
	
	
	public PulseVO() {
	}
	public PulseVO(String emailId, String name, String facebookId,
			double longitude, double latitude, String locationString,
			State state, String othersFacebookId) {
		super();
		this.emailId = emailId;
		this.name = name;
		this.facebookId = facebookId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.locationString = locationString;
		this.state = state;
		this.othersFacebookId = othersFacebookId;
	}
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
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
	public void setState(State state) {
		this.state = state;
	}
	public State getState() {
		return state;
	}
	public void setOthersFacebookId(String othersFacebookId) {
		this.othersFacebookId = othersFacebookId;
	}
	public String getOthersFacebookId() {
		return othersFacebookId;
	}
}
