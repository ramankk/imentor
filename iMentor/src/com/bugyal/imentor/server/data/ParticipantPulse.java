package com.bugyal.imentor.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ParticipantPulse {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String emailId;
	
	@Persistent
	private String name;

	@Persistent
	private String facebookId;
	
	@Persistent
	private double longitude; 
	
	@Persistent
	private double latitude;

	@Persistent
	private String locationString;

	@Persistent
	private boolean isMentor;
	
	
	public ParticipantPulse(String emailId, String name,
			String facebookId, double longitude, double latitude,
			String locationString, boolean isMentor) {
		super();
		this.emailId = emailId;
		this.name = name;
		this.facebookId = facebookId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.locationString = locationString;
		this.isMentor = isMentor;
	}
	
	public Key getKey() {
		return key;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmailId() {
		return emailId;
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
	
	public boolean isMentor() {
		return isMentor;
	}

	public void setMentor(boolean isMentor) {
		this.isMentor = isMentor;
	}
	
}
