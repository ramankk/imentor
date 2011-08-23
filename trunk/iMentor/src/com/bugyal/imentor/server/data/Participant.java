package com.bugyal.imentor.server.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.server.util.MyGeocellManager;
import com.google.appengine.api.datastore.Key;

/**
 * Format:
 * 
 * - hasKnowledge, List<Subject, SkillLevel> - needsKnowledge, List<Subject,
 * SkillLevel>
 * 
 * - Name, Qualification, Location,
 * 
 * @author raman (raman@bugyal.com)
 * 
 */
@PersistenceCapable
public class Participant implements LocationCapable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String name;

	@Persistent
	private String email;

	@Persistent
	private String qualification;

	@Persistent
	private String gender;

	@Persistent
	private Date dateOfBirth;

	@Persistent(serialized = "true")
	private List<Knowledge> knowledge;

	@Persistent
	private Set<Key> mentors; // points to another participant

	@Persistent
	private Set<Key> mentees; // points to another participant

	@Persistent
	private List<Key> coParticipants; // social-group of participants

	@Persistent
	private boolean isNotesPublic = true;

	@Persistent
	private boolean isNotOnline = false; // is true when participant has online
											// presence and has email.

	@Persistent
	private Key creator; // key of the creator participant.

	@Persistent
	private boolean isActive = true;

	@Persistent(serialized = "true")
	private List<Note> notes;

	@Persistent
	private List<String> interests; // TODO(understand the need)

	@Persistent
	private List<String> hasSubjects;

	@Persistent
	private List<String> needSubjects;

	@Persistent
	private double longitude;

	@Persistent
	private double latitude;

	@Persistent
	private int activeRadius;

	@Persistent
	private String locationString;

	@Persistent
	private long lastModifiedTime;

	@Persistent
	private long createdTime;

	@PersistenceCapable
	public static class Note implements Serializable {

		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Key key;

		@Persistent
		private boolean isPublic = true;

		@Persistent
		private Date date;

		@Persistent
		private String note;

		@Persistent
		private Key noter;

		public boolean isPublic() {
			return isPublic;
		}

		public Note setPublic(boolean isPublic) {
			this.isPublic = isPublic;
			return this;
		}

		public Date getDate() {
			return date;
		}

		public Note setDate(Date date) {
			this.date = date;
			return this;
		}

		public String getNote() {
			return note;
		}

		public Note setNote(String note) {
			this.note = note;
			return this;
		}

		Key getNoter() {
			return noter;
		}

		Note setNoter(Key noter) {
			this.noter = noter;
			return this;
		}

		Note(String note, Date date, Key noter, boolean isPublic) {
			super();
			this.note = note;
			this.date = date;
			this.noter = noter;
			this.isPublic = isPublic;
		}
	}

	@PersistenceCapable
	public static class Knowledge implements Serializable {

		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Key key;

		@Persistent
		private boolean has; // true: if has, false if needed.

		@Persistent
		private String subject;

		@Persistent
		private int level;

		@Persistent
		private Key suggestedBy; // participant who suggest this information.

		public String getSubject() {
			return subject;
		}

		public int getLevel() {
			return level;
		}

		public Knowledge setLevel(int level) {
			this.level = level;
			return this;
		}

		Key getSuggestedBy() {
			return suggestedBy;
		}

		Knowledge(String subject, int level, Key suggestedBy, boolean has) {
			super();
			this.subject = subject;
			this.level = level;
			this.suggestedBy = suggestedBy;
			this.has = has;
		}

		public boolean has() {
			return has;
		}

		public boolean needs() {
			return !has;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (has ? 1231 : 1237);
			result = prime * result
					+ ((subject == null) ? 0 : subject.hashCode());
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
			Knowledge other = (Knowledge) obj;
			if (has != other.has)
				return false;
			if (subject == null) {
				if (other.subject != null)
					return false;
			} else if (!subject.equals(other.subject))
				return false;
			return true;
		}
	}

	public String getName() {
		return name;
	}

	Participant setName(String name) {
		this.name = name;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public String getEmail() {
		return email;
	}

	public String getQualification() {
		return qualification;
	}

	public Participant setQualification(String qualification) {
		this.qualification = qualification;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public Participant setDateOfBirth(Date dateOfBirth) {
		this.lastModifiedTime = System.currentTimeMillis();
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public Point getLocation() {
		return new Point(latitude, longitude);
	}

	public Participant setLocation(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		this.locationString = location.getLocationString();
		this.activeRadius = location.getActiveRadius();

		Point p = new Point(latitude, longitude);
		this.geocells = MyGeocellManager.generateGeoCell(p);
		for (String c : geocells) {
			System.out.println("Adding geocell.. " + c + " for " + getName());
		}
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	List<Knowledge> getNeed() {
		return filter(false);
	}

	private List<Knowledge> filter(boolean required) {
		List<Knowledge> kn = new ArrayList<Knowledge>();
		for (Knowledge k : knowledge) {
			if (k.has() == required) {
				kn.add(k);
			}
		}
		return kn;
	}

	Participant addKnowledge(Knowledge k) {
		// TODO(Sridhar, raman) : FIX ME 
		if (this.knowledge == null) {
			this.knowledge = new ArrayList<Knowledge>();
		}
		
		this.knowledge.add(k);
		if (k.has()) {
			hasSubjects.add(k.getSubject());
		} else {
			needSubjects.add(k.getSubject());
		}
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public void clearSubjects(){
		hasSubjects.clear();
		needSubjects.clear();
	}
	
	List<Knowledge> getHas() {
		return filter(true);
	}

	@SuppressWarnings("unchecked")
	List<Key> getMentors() {
		return (List<Key>) mentors;
	}

	@SuppressWarnings("unchecked")
	Participant setMentors(List<Key> mentors) {
		this.mentors = (Set<Key>) mentors;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	Participant addMentor(Key mentor) {
		this.mentors.add(mentor);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	Participant removeMentor(Key mentor) {
		this.mentors.remove(mentor);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	@SuppressWarnings("unchecked")
	List<Key> getMentees() {
		return (List<Key>) mentees;
	}

	@SuppressWarnings("unchecked")
	Participant setMentees(List<Key> mentees) {
		this.mentees = (Set<Key>) mentees;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	Participant addMentee(Key mentee) {
		this.mentees.add(mentee);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	Participant removeMentee(Key mentee) {
		this.mentees.remove(mentee);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	List<Key> getCoParticipants() {
		return coParticipants;
	}

	Participant setCoParticipants(List<Key> coParticipants) {
		this.coParticipants = coParticipants;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public boolean isNotesPublic() {
		return isNotesPublic;
	}

	public Participant setNotesPublic(boolean isNotesPublic) {
		this.isNotesPublic = isNotesPublic;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	List<Note> getNotes() {
		return notes;
	}

	// Notes is meant to be only add-only, so no setter for it.
	Participant addNotes(Note note) {
		this.notes.add(note);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public List<String> getInterests() {
		return interests;
	}

	public Participant setInterests(List<String> interests) {
		this.interests = interests;
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public Participant addInterest(String interest) {
		this.interests.add(interest);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public Participant removeInterest(String interest) {
		this.interests.remove(interest);
		this.lastModifiedTime = System.currentTimeMillis();
		return this;
	}

	public Key getKey() {
		return key;
	}

	boolean isActive() {
		return isActive;
	}

	void setActive(boolean isActive) {
		this.isActive = isActive;
		this.lastModifiedTime = System.currentTimeMillis();
	}

	boolean isNotOnline() {
		return isNotOnline;
	}

	Key getCreator() {
		return creator;
	}

	private void init() {
		this.interests = new ArrayList<String>();
		this.mentees = new HashSet<Key>();
		this.mentors = new HashSet<Key>();
		this.knowledge = new ArrayList<Knowledge>();
		this.coParticipants = new ArrayList<Key>();
		this.notes = new ArrayList<Note>();
		this.hasSubjects = new ArrayList<String>();
		this.needSubjects = new ArrayList<String>();
		this.createdTime = System.currentTimeMillis();
		this.lastModifiedTime = System.currentTimeMillis();
	}

	Participant(String name, String gender, Location location, String email) {
		this.name = name;
		this.email = email;
		this.isNotOnline = false;
		this.creator = null;
		this.gender = gender;
		setLocation(location);
		init();
	}

	Participant(String name, String gender, Location location, Key creatorsKey) {
		this.name = name;
		this.email = null;
		this.isNotOnline = true;
		this.creator = creatorsKey;
		setLocation(location);
		init();
	}

	public static Participant createParticipantForTest(String name,
			Location location, String email) {
		return new Participant(name, "f", location, email);
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
		Participant other = (Participant) obj;
		if (key == null || other.getKey() == null) {
			return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + " " + key;
	}

	@Persistent
	private List<String> geocells;

	@Override
	public List<String> getGeocells() {
		return geocells;
	}

	@Override
	public String getKeyString() {
		return key.toString();
	}

	public Location getLoc() {
		return new Location(this.latitude, this.longitude, this.locationString,
				this.activeRadius);
	}

	public List<String> getHasSubjects() {
		return hasSubjects;
	}

	public List<String> getNeedSubjects() {
		return needSubjects;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

}
