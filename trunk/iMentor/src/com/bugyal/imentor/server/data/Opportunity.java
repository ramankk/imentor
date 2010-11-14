package com.bugyal.imentor.server.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.server.util.MyGeocellManager;
import com.google.appengine.api.datastore.Key;

/**
 * Opputunites:
 * 
 * Format:: <Location, List<Subject>, #-of-mentors, bool is_active,
 * List<Contacts>, Int Priority> Show location with Map etc. Priority
 * (trust-level of creator * priority assigned by creator)
 * 
 * Ability to be searched By Location (region) By Subject By Priority
 * 
 * @author raman (raman@bugyal.com)
 * 
 */
@PersistenceCapable
public class Opportunity implements LocationCapable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private double latitude;

	@Persistent
	private double longitude;

	@Persistent
	private String locationString;

	@Persistent
	private List<String> geocells;

	@Persistent
	private List<String> subjects;

	@Persistent
	private int requiredMentors;

	@Persistent
	private boolean active;

	@Persistent
	private List<Key> contacts; // existing mentors, or who found the
	// oppurtunity.

	@Persistent
	private int priority;

	@Persistent
	private List<ChangeInfo> changeInfo;
	
	// TODO(raman): Add notes in oppurtunity as well. (also in oppVO)

	@PersistenceCapable
	static class ChangeInfo implements Serializable {

		@Persistent
		private Key modifier;

		@Persistent
		private String change;

		public Key getModifier() {
			return modifier;
		}

		public String getChange() {
			return change;
		}

		public ChangeInfo(Key modifier, String change) {
			super();
			this.modifier = modifier;
			this.change = change;
		}
	}

	Opportunity(Location location, List<String> subjects, int requiredMentors,
			List<Participant> contacts, int priority) {
		super();
		this.subjects = subjects;
		this.requiredMentors = requiredMentors;

		this.priority = priority;

		this.contacts = new ArrayList<Key>();

		if (contacts != null) {
			for (Participant m : contacts) {
				this.contacts.add(m.getKey());
			}
		}
		setLocation(location);
		this.active = true; // active when created.
	}

	public Point getLocation() {
		return new Point(latitude, longitude);
	}
	
	public Location getLoc() {
		// TODO(raman): set some appropriate value than 10. 
		return new Location(latitude, longitude, locationString, 10);
	}

	public void setLocation(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		this.setLocationString(location.getLocationString());

		this.geocells = MyGeocellManager.generateGeoCell(new Point(location
				.getLatitude(), location.getLongitude()));
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void addSubject(String subject, Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(), "Added subject "
				+ subject));
		this.subjects.add(subject);
	}

	public void deleteSubject(String subject, Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(), "Removed subject "
				+ subject));
		this.subjects.remove(subject);
	}

	public int getRequiredMentors() {
		return requiredMentors;
	}

	public void setRequiredParticipants(int requiredParticipants,
			Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(),
				"Changed requirement from " + this.requiredMentors + " to "
						+ requiredParticipants));
		this.requiredMentors = requiredParticipants;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active, Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(),
				"Changed active state from " + this.active + " to " + active));
		this.active = active;
	}

	List<Key> getContacts() {
		return contacts;
	}

	public void addContact(Participant contact, Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(), "Added new contact "
				+ contact.getName()));
		this.contacts.add(contact.getKey());
	}

	public void removeContact(Participant contact, Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(), "Removed contact "
				+ contact.getName()));
		this.contacts.remove(contact.getKey());
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority, Participant who) {
		this.changeInfo.add(new ChangeInfo(who.getKey(),
				"Changed priority from " + this.priority + " to " + priority));
		this.priority = priority;
	}

	public List<ChangeInfo> getChangeInfo() {
		return changeInfo;
	}

	public Key getKey() {
		return key;
	}

	@Override
	public List<String> getGeocells() {
		return geocells;
	}

	@Override
	public String getKeyString() {
		return key.toString();
	}

	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	public String getLocationString() {
		return locationString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Opportunity other = (Opportunity) obj;
		if (key == null || other.getKey() == null) {
			return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}
