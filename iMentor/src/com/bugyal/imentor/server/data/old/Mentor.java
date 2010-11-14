package com.bugyal.imentor.server.data.old;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.bugyal.imentor.server.data.Location;
import com.google.appengine.api.datastore.Key;

// TODO(raman): Should Mentor and Mentee be single entity ?
// MUser

/**
 * Mentors:: 
 * 
 * Format:: <Name, qualification, Location, comfort_area,
 * List<<Subject, skill_level>, List<Interests>> 
 * 
 * Didn’t want to include qualification, but added it as mechanism to attract more mentors. 
 * 
 * Ability to be searched (by other mentors) 
 *   By Location 
 *   By subject, skill_level
 *
 * @author raman (raman@bugyal.com)
 *
 */
@PersistenceCapable
public class Mentor {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String quaification;
	
	@Persistent
	private List<Key> mentees; // list of mentees.
 	
	@Persistent
	private Location location;
	
	@Persistent
	private List<String> interest;

	public Mentor(String name, String quaification, Location location,
			List<String> interest) {
		super();
		this.name = name;
		this.quaification = quaification;
		this.location = location;
		this.interest = interest;
		this.mentees = new ArrayList<Key>();
	}

	public Key getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuaification() {
		return quaification;
	}

	public void setQuaification(String quaification) {
		this.quaification = quaification;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<String> getInterest() {
		return interest;
	}

	public void setInterest(List<String> interest) {
		this.interest = interest;
	}

	public List<Key> getMentees() {
		return mentees;
	}

	public void setMentees(List<Key> mentees) {
		this.mentees = mentees;
	}
	
	public void addMentee(Mentee mentee) {
		this.mentees.add(mentee.getKey());
	}
}
