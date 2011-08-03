package com.bugyal.imentor.server.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.server.ParticipantManager;
import com.bugyal.imentor.server.util.MyGeocellManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

public class ParticipantManagerImpl implements ParticipantManager {

	private static final Logger LOG = Logger
			.getLogger(ParticipantManagerImpl.class.getCanonicalName());

	@Override
	public void addCoParticipant(Participant i, Participant him) {
		i.getCoParticipants().add(him.getKey());
		save(i);
	}

	@Override
	public void addHasKnowledge(Participant i, String s, int l,
			Participant suggestedBy) {
		i.addKnowledge(new Participant.Knowledge(s, l, suggestedBy.getKey(),
				true));
		save(i);
	}

	@Override
	public void addMentee(Participant i, Participant mentee) {
		addMentor(mentee, i);
	}

	// TODO(raman): in a txn.
	@Override
	public void addMentor(Participant i, Participant mentor) {
		i.getMentors().add(mentor.getKey());
		mentor.getMentees().add(i.getKey());
		save(i, mentor);
	}

	@Override
	public void addNeedKnowledge(Participant i, String s, int l,
			Participant suggestedBy) {
		i.addKnowledge(new Participant.Knowledge(s, l, suggestedBy.getKey(),
				false));
		save(i);
	}

	@Override
	public void addNotes(Participant i, Participant him, String notes) {
		i.getNotes().add(
				new Participant.Note(notes, new Date(), him.getKey(), i
						.isNotesPublic()));
	}

	@Override
	public void changeName(Participant i, String newName) {
		i.setName(newName);
	}

	@Override
	public Participant createParticipant(String name, Location location,
			String email) throws MentorException {
		Participant e = findParticipantByEmail(email);
		if (e != null) {
			throw new MentorException("Participant with email " + email
					+ " already exists.");
		}
		Participant i = new Participant(name, location, email);
		save(i);
		return i;
	}

	@Override
	public Participant createParticipant(String name, Location location,
			Participant creator) {
		Participant i = new Participant(name, location, creator.getKey());
		save(i);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Participant findParticipantByEmail(String email)
			throws MentorException {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Participant> results = null;

		try {
			Query q = pm.newQuery(Participant.class, "email == param");
			q.declareParameters("String param");

			results = (List<Participant>) q.execute(email);
			for (Participant p : results) {
				System.out.println("Returning .. " + p.getName());
			}
			q.closeAll();
		} finally {
			pm.close();
		}

		if (results.size() == 0) {
			return null; // no participant is found.
		} else if (results.size() > 1) {
			throw new MentorException(
					"Multiple participants found with same email, needs cleanup. Email:: "
							+ email);
		}

		return results.get(0);
	}

	@Override
	public List<Participant> getMentees(Participant i) {
		return keysToParticipants(i.getMentees());
	}

	@Override
	public List<Participant> getMentors(Participant i) {
		return keysToParticipants(i.getMentors());
	}

	private List<Participant> keysToParticipants(List<Key> keys) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Participant> results = new ArrayList<Participant>();

		try {
			for (Key k : keys) {
				results.add(pm.getObjectById(Participant.class, k));
			}
		} finally {
			pm.close();
		}

		return results;
	}

	@Override
	public void recommend(Participant i, Participant him)
			throws MentorException {
		throw new MentorException("Not implemented");
	}

	@Override
	public void save(Participant... participants) {
		System.out.println("Trying to save :: " + participants);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		for (Participant p : participants) {
			LOG.info("Saving.. " + p);
		}
		try {
			pm.makePersistentAll(participants);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> searchParticipantsByInterest(String interest) {
		Preconditions.checkNotNull(interest);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Participant> results = null;

		try {
			Query q = pm.newQuery(Participant.class, "interests == param");
			q.declareParameters("String param");

			results = (List<Participant>) q.execute(interest);
			for (Participant p : results) {
				System.out.println("Returning .. " + p.getName());
			}
			q.closeAll();
		} finally {
			pm.close();
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> searchParticipantsBySubject(String subject,
			Location l, boolean has) {
		Preconditions.checkNotNull(subject);
		Preconditions.checkNotNull(l);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Participant> results = null;

		Point center = new Point(l.getLatitude(), l.getLongitude());

		List<Object> params = new ArrayList<Object>();
		params.add(subject);

		String filter = has ? "hasSubjects == param" : "needSubjects == param";
		GeocellQuery baseQuery = new GeocellQuery(filter, "String param",
				params);

		try {
			results = MyGeocellManager.proximityFetch(center, 30,
					l.getActiveRadius() * 1000, Participant.class, baseQuery,
					pm);
		} finally {
			pm.close();
		}

		return results;
	}

	@Override
	public List<Participant> searchParticipantsBySubjects(
			List<String> subjects, Location l, boolean has)
			throws MentorException {

		Preconditions.checkNotNull(subjects);
		Preconditions.checkNotNull(l);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Participant> results = null;

		Point center = new Point(l.getLatitude(), l.getLongitude());
		List<Object> params = new ArrayList<Object>();
		params.add(subjects);

		// String filter = has ? "hasSubjects == params" :
		// "needSubjects == params";
		String filter = has ? "hasSubjects" : "needSubjects";

		StringBuilder queryString = new StringBuilder("(");

		// TODO(sridhar,raman): Make it to a prepared query.
		boolean first = true;
		for (String s : subjects) {
			if (first) {
				first = false;
				queryString.append(filter + " == \"" + s +"\"");
			} else {
				queryString.append(" || " + filter + " == \"" + s +"\"");
			}
		}
		queryString.append(")");

		GeocellQuery query = new GeocellQuery(queryString.toString(), null, null);

		try {
			results = MyGeocellManager.proximityFetch(center, 30,
					l.getActiveRadius() * 1000, Participant.class, query, pm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return results;
	}

	@Override
	public Participant findById(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Participant p = null;
		try {
			p = pm.getObjectById(Participant.class, key);
		} finally {
			pm.close();
		}

		return p;
	}

}