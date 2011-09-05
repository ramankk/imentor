package com.bugyal.imentor.server.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.frontend.server.LRUCache;
import com.bugyal.imentor.frontend.server.StatsServlet;
import com.bugyal.imentor.frontend.server.StatsServlet.AverageStat;
import com.bugyal.imentor.server.ParticipantManager;
import com.bugyal.imentor.server.util.MyGeocellManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

public class ParticipantManagerImpl implements ParticipantManager {

	private static final long LOCAL_STREAM_PERIOD = 20 * 24 * (60 * 60 * 1000);

	private static final Logger LOG = Logger
			.getLogger(ParticipantManagerImpl.class.getCanonicalName());
	private LRUCache<String, Participant> cache = new LRUCache<String, Participant>(
			10);

	@Override
	public void addCoParticipant(Participant i, Participant him) {
		i.getCoParticipants().add(him.getKey());
		save(i);		
	}

	static AverageStat addHasKnowledgeTimeState = StatsServlet
			.createAverageStat("addHasKnowledge_total_time");

	@Override
	public void addHasKnowledge(Participant i, List<String> hasSubjects, int l,
			Participant suggestedBy) {
		long t = System.currentTimeMillis();
		for (String has : hasSubjects) {
			i.addKnowledge(new Participant.Knowledge(has, l, suggestedBy
					.getKey(), true));
		}
		save(i);
		addHasKnowledgeTimeState.inc(System.currentTimeMillis() - t);
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

	static AverageStat addNeedKnowledgeTimeState = StatsServlet
			.createAverageStat("addNeedKnowledge_total_time");

	@Override
	public void addNeedKnowledge(Participant i, List<String> needSubjects,
			int l, Participant suggestedBy) {
		long t = System.currentTimeMillis();

		for (String need : needSubjects) {
			i.addKnowledge(new Participant.Knowledge(need, l, suggestedBy
					.getKey(), false));
		}
		save(i);
		addNeedKnowledgeTimeState.inc(System.currentTimeMillis() - t);
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

	static AverageStat createParticipantByEmailTimeState = StatsServlet
			.createAverageStat("createParticipant_ByEmail_total_time");

	@Override
	public Participant createParticipant(String name, String gender,
			Location location, String email) throws MentorException {
		long t = System.currentTimeMillis();
		Participant e = findParticipantByEmail(email);
		if (e != null) {
			throw new MentorException("Participant with email " + email
					+ " already exists.");
		}
		Participant i = new Participant(name, gender, location, email);
		save(i);
		createParticipantByEmailTimeState.inc(System.currentTimeMillis() - t);
		return i;
	}

	static AverageStat createParticipantByParticipantTimeState = StatsServlet
			.createAverageStat("createParticipant_ByParticipant_total_time");

	@Override
	public Participant createParticipant(String name, String gender,
			Location location, Participant creator) {
		long t = System.currentTimeMillis();
		Participant i = new Participant(name, gender, location,
				creator.getKey());
		save(i);
		createParticipantByParticipantTimeState.inc(System.currentTimeMillis()
				- t);
		return i;
	}

	static AverageStat findParticipantByEmailTimeState = StatsServlet
			.createAverageStat("findParticipantByEmail_total_time");

	@SuppressWarnings("unchecked")
	@Override
	public Participant findParticipantByEmail(String email)
			throws MentorException {
		long t = System.currentTimeMillis();

		Participant participant = cache.get(email);
		if (participant != null) {
			return participant;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Participant> results = null;

		try {
			Query q = pm.newQuery(Participant.class, "email == param");
			q.declareParameters("String param");

			results = (List<Participant>) q.execute(email);
			for (Participant p : results) {
				System.out.println("Returning .. " + p.getName());
				System.out.println("Location : " + p.getLocation());
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

		cache.put(email, results.get(0));
		findParticipantByEmailTimeState.inc(System.currentTimeMillis() - t);
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

	static AverageStat keysToParticipantsTimeState = StatsServlet
			.createAverageStat("keysToParticipants_total_time");

	private List<Participant> keysToParticipants(List<Key> keys) {
		long t = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Participant> results = new ArrayList<Participant>();

		try {
			for (Key k : keys) {
				results.add(pm.getObjectById(Participant.class, k));
			}
		} finally {
			pm.close();
		}
		keysToParticipantsTimeState.inc(System.currentTimeMillis() - t);
		return results;
	}

	@Override
	public void recommend(Participant i, Participant him)
			throws MentorException {
		throw new MentorException("Not implemented");
	}
	
	static AverageStat saveParticipantTimeState = StatsServlet
	.createAverageStat("saveParticipant_total_time");
	@Override
	public void save(Participant... participants) {
		long t = System.currentTimeMillis();
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
		saveParticipantTimeState.inc(System.currentTimeMillis() - t);
	}

	static AverageStat searchParticipantsByInterestTimeState = StatsServlet
			.createAverageStat("searchParticipantsByInterest_total_time");

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> searchParticipantsByInterest(String interest) {
		long t = System.currentTimeMillis();
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
		keysToParticipantsTimeState.inc(System.currentTimeMillis() - t);
		return results;
	}

	static AverageStat deleteParticipantsTimeState = StatsServlet
			.createAverageStat("deleteParticipants_total_time");

	@Override
	public long deleteParticipants() {
		long t = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long n;
		try {
			Query q = pm.newQuery(Participant.class);
			n = q.deletePersistentAll();
			q.closeAll();

		} finally {
			pm.close();
		}
		deleteParticipantsTimeState.inc(System.currentTimeMillis() - t);
		return n;
	}

	static AverageStat searchParticipantsBySubjectTimeState = StatsServlet
			.createAverageStat("searchParticipantsBySubject_total_time");

	@Override
	public List<Participant> searchParticipantsBySubject(String subject,
			Location l, boolean has) {
		long t = System.currentTimeMillis();
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
		searchParticipantsBySubjectTimeState
				.inc(System.currentTimeMillis() - t);
		return results;
	}

	static AverageStat searchParticipantsByLocationTimeState = StatsServlet
			.createAverageStat("searchParticipantsByLocation_total_time");

	@Override
	public List<Participant> searchParticipantsByLocation(Location l)
			throws MentorException {
		long t = System.currentTimeMillis();

		Preconditions.checkNotNull(l);
		long checkTime = System.currentTimeMillis() - LOCAL_STREAM_PERIOD;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Participant> results = null;
		Point center = new Point(l.getLatitude(), l.getLongitude());
		List<Object> params = new ArrayList<Object>();
		params.add(checkTime);

		String filter = "lastModifiedTime >= updateTimeP";
		GeocellQuery query = new GeocellQuery(filter, "long updateTimeP",
				params);
		try {
			System.out.println(t);
			results = GeocellManager
					.proximityFetch(center, 50, l.getActiveRadius() * 1000,
							Participant.class, query, pm, 9);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		searchParticipantsByLocationTimeState.inc(System.currentTimeMillis()
				- t);
		return results;
	}

	static AverageStat searchParticipantsBySubjectsTimeState = StatsServlet
			.createAverageStat("searchParticipantsBySubjects_total_time");

	@Override
	public List<Participant> searchParticipantsBySubjects(
			List<String> subjects, Location l, boolean has)
			throws MentorException {
		long t = System.currentTimeMillis();

		Preconditions.checkNotNull(subjects);
		Preconditions.checkNotNull(l);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Participant> results = new ArrayList<Participant>();

		Point center = new Point(l.getLatitude(), l.getLongitude());
		List<Object> params = new ArrayList<Object>();
		params.add(subjects);

		String filter = has ? "hasSubjects" : "needSubjects";
		filter += ".contains(subjectsP)";

		GeocellQuery query = new GeocellQuery(filter, "String subjectsP",
				params);

		try {
			results = GeocellManager
					.proximityFetch(center, 30, l.getActiveRadius() * 1000,
							Participant.class, query, pm, 8);
			System.out.println(System.currentTimeMillis() - t);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		searchParticipantsBySubjectsTimeState.inc(System.currentTimeMillis()
				- t);
		return results;
	}

	static AverageStat findByIdKeyTimeState = StatsServlet
			.createAverageStat("findById(Key_total_time");

	@Override
	public Participant findById(Key key) {
		long t = System.currentTimeMillis();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Participant p = null;
		try {
			p = pm.getObjectById(Participant.class, key);

			// Magic to load knowledge and notes.
			p.getHas();
			p.getNotes();

		} finally {
			pm.close();
		}

		findByIdKeyTimeState.inc(System.currentTimeMillis() - t);
		return p;
	}

	static AverageStat addMentorToMenteeTimeState = StatsServlet
			.createAverageStat("addMentorToMentee_total_time");
	@Override
	public boolean addMentorToMentee(Participant mentor, Participant mentee) {

		long t = System.currentTimeMillis();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Participant imentor = pm.getObjectById(Participant.class,
					mentor.getKey());
			imentor.addMentee(mentee.getKey());
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			addMentorToMenteeTimeState.inc(System.currentTimeMillis() - t);
			return false;
		}
		try {
			tx.begin();
			Participant imentee = pm.getObjectById(Participant.class,
					mentee.getKey());
			imentee.addMentor(mentor.getKey());
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			addMentorToMenteeTimeState.inc(System.currentTimeMillis() - t);
			return false;
		}
		addMentorToMenteeTimeState.inc(System.currentTimeMillis() - t);
		return true;
	}
	
	static AverageStat deleteMentorFromMenteeTimeState = StatsServlet
	.createAverageStat("deleteMentorFromMentee_total_time");
	@Override
	public boolean deleteMentorFromMentee(Participant mentor, Participant mentee) {

		long t = System.currentTimeMillis();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Participant imentor = pm.getObjectById(Participant.class, mentor.getKey());
			imentor.removeMentee(mentee.getKey());
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			deleteMentorFromMenteeTimeState.inc(System.currentTimeMillis() - t);
			return false;
		}
		try {
			tx.begin();
			Participant imentee = pm.getObjectById(Participant.class,
			mentee.getKey());
			imentee.removeMentor(mentor.getKey());
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			deleteMentorFromMenteeTimeState.inc(System.currentTimeMillis() - t);
			return false;
		}
		deleteMentorFromMenteeTimeState.inc(System.currentTimeMillis() - t);
		return true;
	}
	
	@Override
	public void createComment(Feedback... strings) throws MentorException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistentAll(strings);
		}catch(Exception e){
			
		}
		
	}
}
