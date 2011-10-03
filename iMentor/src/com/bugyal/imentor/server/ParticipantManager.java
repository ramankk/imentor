package com.bugyal.imentor.server;

import java.util.List;

import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.server.data.Feedback;
import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Participant;
import com.bugyal.imentor.server.data.ParticipantPulse;
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
	Participant createParticipant(String name, String gender,
			Location location, String email, String facebookId)
			throws MentorException;
	
	Participant createParticipant(String name, String gender, Location location,
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

	void addHasKnowledge(Participant i,  List<String> hasSubjects, int l, Participant suggestedBy)
			throws MentorException;

	void addNeedKnowledge(Participant i, List<String> subjects, int l,
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

	boolean addMentorToMentee(Participant mentor, Participant mentee);

	void createComment(Feedback... strings) throws MentorException;
	
	boolean createPulse(ParticipantPulse... strings) throws MentorException;

	boolean deleteMentorFromMentee(Participant mentor, Participant mentee);

	List<Participant> findParticipantsByIds(List<Key> keys);
	
	boolean deleteOpportuniryFromParticipant(Key partcipantkey, Key opportunitykey );

	boolean saveOpportunityToParticipant(Key participantkey,
			Key opportunitykey, boolean isMentoring);

	List<ParticipantPulse> getTopEntries(int range) throws MentorException;
	
}
