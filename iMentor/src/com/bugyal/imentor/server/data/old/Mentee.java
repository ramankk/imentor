package com.bugyal.imentor.server.data.old;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.bugyal.imentor.server.data.Location;
import com.google.appengine.api.datastore.Key;

/**
 * Mentee: 
 * 
 * Format:: <Name, Location, photo, DOB, class, List<Subject,
 * Profeciency_levels>, List<Interests, levels>, List<Mentors>> 
 * 
 * Ability to be searched 
 * 	 By Subject 
 *   By Interest
 *   By Mentor
 *   
 *   Ability to add more owners for the oppurtunity.
 * 
 * @author raman (raman@bugyal.com)
 * 
 */

@PersistenceCapable
public class Mentee {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Location location;
	
	@Persistent
	private Date dateOfBirth;
	
	@Persistent
	private String standard;
	
	@Persistent
	private List<String> interests;
	
	@Persistent
	private List<Key> mentors;

	public Mentee(Location location, Date dateOfBirth, String standard,
			List<String> interests) {
		super();
		this.location = location;
		this.dateOfBirth = dateOfBirth;
		this.standard = standard;
		this.interests = interests;
		this.mentors = new ArrayList<Key>();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public List<String> getInterests() {
		return interests;
	}

	public void setInterests(List<String> interests) {
		this.interests = interests;
	}

	public List<Key> getMentors() {
		return mentors;
	}

	public void setMentors(List<Key> mentors) {
		this.mentors = mentors;
	}

	public Key getKey() {
		return key;
	}
	
	
}
