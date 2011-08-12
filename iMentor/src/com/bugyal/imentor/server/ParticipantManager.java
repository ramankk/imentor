package com.bugyal.imentor.server;

import java.util.List;

import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Participant;
import com.google.appengine.api.datastore.Key;

public interface ParticipantManager {
	/**
	 * Creates a participant and persists into data store.
	 * 
	 * @param name
	 *            Name of the participant
	 * @param email
	 *            Unique Identification, (not mandatory)
	 * @return Participant object.
	 * 
	 * @throws MentorException
	 *             if a participant already exists with same email.
	 */
	Participant createParticipant(String name, Location location, String email)
			throws MentorException;

	Participant createParticipant(String name, Location location,
			Participant creator) throws MentorException;

	Participant findParticipantByEmail(String email) throws MentorException;

	List<Participant> searchParticipantsByInterest(String interest)
			throws MentorException;

	// TODO(raman): refactor when needed to search(By.xyz());
	List<Participant> searchParticipantsBySubject(String subject, Location l,
			boolean has) throws MentorException;

	List<Participant> searchParticipantsBySubjects(List<String> subjects, Location l,
			boolean has) throws MentorException;
	
	List<Participant> getMentors(Participant i) throws MentorException;

	List<Participant> getMentees(Participant i) throws MentorException;

	void addMentor(Participant i, Participant mentor) throws MentorException;

	// should also do the addMentor(mentee, i);
	void addMentee(Participant i, Participant mentee) throws MentorException;

	void changeName(Participant i, String newName) throws MentorException;

	void addHasKnowledge(Participant i, String s, int l, Participant suggestedBy)
			throws MentorException;

	void addNeedKnowledge(Participant i, String s, int l,
			Participant suggestedBy) throws MentorException;

	void save(Participant... participants) throws MentorException;

	void recommend(Participant i, Participant him) throws MentorException;

	void addCoParticipant(Participant i, Participant him)
			throws MentorException;

	void addNotes(Participant i, Participant him, String notes)
			throws MentorException;

	Participant findById(Key id);

	List<Participant> searchParticipantsByLocation(Location l)
			throws MentorException;

	long deleteParticipants();

}
