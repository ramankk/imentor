package com.bugyal.imentor.server.data.old;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Knowledge:: 
 * 
 * Format:: <Topic, Location, Mentor, Skill_level, Suggested_by>
 * 		Suggested_by is self (when mentor himself defines it) 
 * 		Suggested_by is other-mentors-id (when some other mentor says about this mentor)

 * @author raman (raman@bugyal.com)
 *
 */

@PersistenceCapable
public class HasKnowledge {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String topic;
	
	@Persistent
	private Key mentor; // Mentor type entity.
	
	@Persistent
	private short skillLevel;
	
	@Persistent
	private Key suggestedBy; // Mentor type entity.
	
	public HasKnowledge(String topic, Key mentor, short skillLevel, Key suggestedBy) {
		super();
		this.topic = topic;
		this.mentor = mentor;
		this.skillLevel = skillLevel;
		this.suggestedBy = suggestedBy;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Key getMentor() {
		return mentor;
	}

	public void setMentor(Key mentor) {
		this.mentor = mentor;
	}

	public short getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(short skillLevel) {
		this.skillLevel = skillLevel;
	}

	public Key getSuggestedBy() {
		return suggestedBy;
	}

	public void setSuggestedBy(Key suggestedBy) {
		this.suggestedBy = suggestedBy;
	}
}
