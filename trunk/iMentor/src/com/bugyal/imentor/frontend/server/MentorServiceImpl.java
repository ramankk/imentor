package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.beoui.geocell.GeocellUtils;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.frontend.client.MentorService;
import com.bugyal.imentor.frontend.server.StatsServlet.AverageStat;
import com.bugyal.imentor.frontend.shared.MeException;
import com.bugyal.imentor.frontend.shared.MentorsResult;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.MentorManager;
import com.bugyal.imentor.server.OpportunityManager;
import com.bugyal.imentor.server.ParticipantManager;
import com.bugyal.imentor.server.data.Feedback;
import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;
import com.bugyal.imentor.server.data.old.Subject;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MentorServiceImpl extends RemoteServiceServlet implements
		MentorService {

	private static final List<String> SUBJECTS_LIST = new ArrayList<String>();
	private static final Ranker feedRanker = new Ranker().addScorer(
			new DistanceScorer()).addScorer(new SubjectCorrelationScorer());
	private static final ParticipantManager pm = MentorManager.INSTANCE
			.getParticipantManager();
	private static final OpportunityManager om = MentorManager.INSTANCE
			.getOpportunityManager();
	private static final MentorManager mm = MentorManager.INSTANCE;

	private static final String USER_ID = "UserId";
	private static final String PROVIDER_ID = "ProviderId";
	private static final String PROVIDER = "Provider";

	private static final Logger LOG = Logger.getLogger(MentorServiceImpl.class
			.getName());

	static {
		SUBJECTS_LIST.add("Computers");
		SUBJECTS_LIST.add("Physics");
		SUBJECTS_LIST.add("Socialogy");
		SUBJECTS_LIST.add("Mathematics");
	}

	@Override
	public List<OpportunityVO> find(List<String> subjects, ParticipantVO me)
			throws IllegalArgumentException {
		Key key = KeyFactory.createKey(Participant.class.getSimpleName(), me
				.getId());
		Participant p = pm.findById(key);

		List<OpportunityVO> rList = new ArrayList<OpportunityVO>();

		List<Opportunity> oList = null;
		if (subjects != null) {
			oList = mm.getAllOppurtunities(p, subjects);
		} else {
			oList = mm.getAllOppurtunities(p);
		}
		for (Opportunity o : oList) {
			rList.add(ValueObjectGenerator.create(o));
		}

		return rList;
	}

	@Override
	public ParticipantVO create(ParticipantVO p) throws MeException {
		if (p.getId() != null && p.getId() != 0) {
			throw new MeException("Cannot create already created participant");
		}

		Location location = new Location(p.getLatitude(), p.getLongitude(), p
				.getLocationString(), p.getRadius());
		Participant pi = null;
		try {
			pi = pm.createParticipant(p.getName(), p.getGender(), location, p
					.getEmail());
			pm.addHasKnowledge(pi, p.getHasSubjects(), 1, pi);
			pm.addNeedKnowledge(pi, p.getNeedSubjects(), 1, pi);

			save(pi, p);
		} catch (MentorException m) {
			throw new MeException(m.getMessage());
		}
		if (pi != null) {
			return ValueObjectGenerator.create(pi);
		} else {
			return null;
		}
	}

	private void save(Participant pi, ParticipantVO p) throws MentorException {
		Location location = new Location(p.getLatitude(), p.getLongitude(), p
				.getLocationString(), p.getRadius());
		clearSubjects(pi);
		if (p.getHasSubjects() != null) {

			pm.addHasKnowledge(pi, p.getHasSubjects(), 5, pi);
		}

		if (p.getNeedSubjects() != null) {
			pm.addNeedKnowledge(pi, p.getNeedSubjects(), 5, pi);
		}

		pi.setLocation(location);
		pm.save(pi);
	}

	private void clearSubjects(Participant pi) throws MentorException {
		// TODO(sridhar,raman): resolve it.
		pi.clearSubjects();
		pm.save(pi);
	}

	private void save(Opportunity oi, OpportunityVO o) throws MentorException {
		Location location = new Location(o.getLatitude(), o.getLongitude(), o
				.getLocString(), o.getRadius());
		oi.setLocation(location);
		Participant savedBy = pm.findParticipantByEmail(getUserId());

		if (o.getSubjects() != null) {
			oi.resetSubjects(o.getSubjects(), savedBy);
		}

		oi.setPriority(o.getPriority(), savedBy);
		oi.setActive(true, savedBy);
		oi.setRequiredParticipants(o.getRequiredMentors(), savedBy);
		om.update(oi, savedBy);
	}

	@Override
	public OpportunityVO createOpportunity(OpportunityVO o) throws MeException {
		if (o.getId() != null) {
			throw new MeException("Cannot create already created participant");
		}

		Location location = new Location(o.getLatitude(), o.getLongitude(), o
				.getLocString(), o.getRadius());
		Opportunity oi = null;
		List<Participant> contacts = new ArrayList<Participant>();

		Participant participant = null;
		try {
			participant = pm.findParticipantByEmail(getUserId());
			if (participant != null) {
				contacts.add(participant);
			}
		} catch (MentorException e) {
			e.printStackTrace();
		}

		// TODO(raman): Understand why MentorException is not getting thrown.
		oi = om.createOpportunity(location, o.getSubjects(), o
				.getRequiredMentors(), contacts, o.getPriority(), o
				.getMessage(), participant);

		if (oi != null) {
			try {
				participant.addCreatedOpportuny(oi.getKey());
				pm.save(participant);
			} catch (MentorException e) {
				e.printStackTrace();
			}
			return ValueObjectGenerator.create(oi);
		} else {
			return null;
		}
	}

	@Override
	public ParticipantVO update(ParticipantVO p) throws MeException {
		if (p.getId() == null) {
			throw new MeException("New participant, cannot update.");
		}

		Participant pi = null;
		Key key = KeyFactory.createKey(Participant.class.getSimpleName(), p
				.getId());

		try {
			pi = pm.findById(key);
			save(pi, p);
		} catch (MentorException m) {
			throw new MeException(m.getMessage());
		}
		if (pi != null) {
			return ValueObjectGenerator.create(pi);
		} else {
			return null;
		}
	}

	@Override
	public OpportunityVO updateOpportunity(OpportunityVO o) throws MeException {
		if (o.getId() == null) {
			throw new MeException("New opportunity, cannot update.");
		}

		Opportunity oi = null;
		Key key = KeyFactory.createKey(Opportunity.class.getSimpleName(), o
				.getId());

		try {
			oi = om.findById(key);
			save(oi, o);
		} catch (MentorException m) {
			throw new MeException(m.getMessage());
		}
		if (oi != null) {
			return ValueObjectGenerator.create(oi);
		} else {
			return null;
		}
	}

	@Override
	public List<String> getSubjects() throws MeException {
		List<String> list = new ArrayList<String>();

		Subject sub[] = Subject.values();

		for (Subject s : sub) {
			list.add(s.toString());
		}
		return list;
	}

	@Override
	public void generateRandomData(int range) throws MeException {
		try {
			new DataGenerator(range);
		} catch (MentorException e) {
			e.printStackTrace();
			throw new MeException(e.getMessage());
		}
	}

	@Override
	public long deleteRecords() {
		long count = 0;
		try {
			count += pm.deleteParticipants();
			count += om.deleteOpportunities();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public SearchResponse filterList(double latitude, double longitude,
			String strlocation, int radius, List<String> hasSubs,
			List<String> needSubs) {
		SearchResponse response = new SearchResponse();
		Point pt = new Point(latitude, longitude);
		try {
			Set<String> hasSubjects = new HashSet<String>();
			Set<String> needSubjects = new HashSet<String>();

			for (String s : hasSubs) {
				hasSubjects.add(s);
			}
			for (String s : needSubs) {
				needSubjects.add(s);
			}

			List<SearchResult> has = new ArrayList<SearchResult>();
			List<SearchResult> need = new ArrayList<SearchResult>();

			Location location = new Location(latitude, longitude, strlocation,
					radius);

			for (Participant p : pm.searchParticipantsBySubjects(needSubs,
					location, true)) {
				List<String> matchingSubs = new ArrayList<String>();
				for (String s : p.getHasSubjects()) {
					if (needSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				has.add(new SearchResult(ValueObjectGenerator.create(p), true,
						matchingSubs, GeocellUtils
								.distance(pt, p.getLocation())));
			}

			for (Participant p : pm.searchParticipantsBySubjects(hasSubs,
					location, false)) {
				List<String> matchingSubs = new ArrayList<String>();
				for (String s : p.getNeedSubjects()) {
					if (hasSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				need.add(new SearchResult(ValueObjectGenerator.create(p),
						false, matchingSubs, GeocellUtils.distance(pt, p
								.getLocation())));
			}

			for (Opportunity o : om.searchOpportunities(location, hasSubs)) {

				List<String> matchingSubs = new ArrayList<String>();
				for (String s : o.getSubjects()) {
					if (hasSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				need.add(new SearchResult(ValueObjectGenerator.create(o),
						matchingSubs, GeocellUtils
								.distance(pt, o.getLocation())));
			}
			response.setHas(has);
			response.setNeed(need);
			return response;
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return response;
	}

	static AverageStat feedToMeTimeStat = StatsServlet.createAverageStat("feed_to_me_total_time");
	
	@Override
	public SearchResponse feedToMe() throws MeException {
		long t = System.currentTimeMillis();
		SearchResponse response = null;
		try {
			Participant pi = pm.findParticipantByEmail(getUserId());
			if (pi == null) {
				return new SearchResponse();
			}
			response = filterList(pi.getLocation().getLat(), pi.getLocation()
					.getLon(), pi.getLoc().getLocationString(), pi.getLoc()
					.getActiveRadius(), pi.getHasSubjects(), pi
					.getNeedSubjects());

			response = filterMe(pi, response);

			feedRanker.rank(response, pi);
			return response;
		} catch (MentorException e) {
			e.printStackTrace();
		} finally {
			feedToMeTimeStat.inc(System.currentTimeMillis() - t);
		}

		return response;
	}

	public ParticipantVO getParticipantVOByEmailId() throws MeException {
		try {
			Participant participant = pm.findParticipantByEmail(getUserId());
			return ValueObjectGenerator.create(participant);
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return null;
	}

	static AverageStat localActivityTimeStat = StatsServlet.createAverageStat("local_activity_total_time");
	
	@Override
	public SearchResponse localActivity() {
		long t = System.currentTimeMillis();
		SearchResponse response = new SearchResponse();
		try {
			List<SearchResult> has = new ArrayList<SearchResult>();
			List<SearchResult> need = new ArrayList<SearchResult>();

			Participant pi = pm.findParticipantByEmail(getUserId());
			if (pi == null) {
				return response;
			}
			Location location = new Location(pi.getLocation().getLat(), pi
					.getLocation().getLon(), pi.getLoc().getLocationString(),
					pi.getLoc().getActiveRadius());

			for (Participant p : pm.searchParticipantsByLocation(location)) {
				double distance = GeocellUtils.distance(pi.getLocation(), p
						.getLocation());
				has.add(new SearchResult(ValueObjectGenerator.create(p), true,
						p.getHasSubjects(), distance));
				need.add(new SearchResult(ValueObjectGenerator.create(p),
						false, p.getNeedSubjects(), distance));
			}

			for (Opportunity o : om.allOpportunites(location)) {
				need.add(new SearchResult(ValueObjectGenerator.create(o), o
						.getSubjects(), GeocellUtils.distance(pi.getLocation(),
						o.getLocation())));
			}
			response.setHas(has);
			response.setNeed(need);
			response = filterMe(pi, response);
			return response;
		} catch (MentorException e) {
			e.printStackTrace();
		} finally {
			localActivityTimeStat.inc(System.currentTimeMillis() - t);
		}
		return response;
	}

	private SearchResponse filterMe(Participant pi, SearchResponse response) {
		String email = pi.getEmail();
		SearchResponse result = new SearchResponse();
		List<SearchResult> hasSub = new ArrayList<SearchResult>();
		List<SearchResult> needSub = new ArrayList<SearchResult>();

		for (SearchResult has : response.getHas()) {
			if (!(has.getP().getEmail().equals(email))) {
				hasSub.add(has);
			}
		}
		for (SearchResult need : response.getNeed()) {
			if (need.isTypeParticipant()) {
				if (!(need.getP().getEmail().equals(email))) {
					needSub.add(need);
				}
			} else {
				needSub.add(need);
			}
		}
		result.setHas(hasSub);
		result.setNeed(needSub);
		return result;
	}

	@Override
	public List<OpportunityVO> getOpportunitiesById() {
		Participant p = null;
		try {
			p = pm.findParticipantByEmail(getUserId());
			if (p == null) {
				return new ArrayList<OpportunityVO>();
			}
			List<Opportunity> opportunities = om.searchOpportunitiesByKey(p
					.getKey());
			return ValueObjectGenerator.createOpportunityVOs(opportunities);
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean createSession(String emailId, String provider,
			String providerId) throws MeException {
		getThreadLocalRequest().getSession().setAttribute(USER_ID, emailId);
		getThreadLocalRequest().getSession().setAttribute(PROVIDER, provider);
		getThreadLocalRequest().getSession().setAttribute(PROVIDER_ID,
				providerId);

		LOG.info("Created session for " + emailId + ", provider: " + provider
				+ ", id: " + providerId);
		System.out.println("Created session for " + emailId + ", provider: "
				+ provider + ", id: " + providerId);
		if (getParticipantVOByEmailId() != null) {
			System.out.println("Session created for returning user.");
			return true;
		} else {
	    	System.out.println("Session created for new user.");
			return false;
		}
	}

	private String getUserId() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				USER_ID);
	}

	@Override
	public boolean deleteSession() throws MeException {
		if (getThreadLocalRequest().getSession().getAttribute(USER_ID) != null) {
			getThreadLocalRequest().getSession().removeAttribute(USER_ID);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addMentorAndMentee(Boolean isHas, String mentorMailId) {
		try {
			Participant mentor = pm.findParticipantByEmail(mentorMailId);
			Participant mentee = pm.findParticipantByEmail(getUserId());
			if (isHas) {
				return pm.addMentorToMentee(mentor, mentee);
			} else {
				return pm.addMentorToMentee(mentee, mentor);
			}
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean deleteMentorAndMentee(Boolean isHas, String mentorMailId) {
		try {
			Participant mentor = pm.findParticipantByEmail(mentorMailId);
			Participant mentee = pm.findParticipantByEmail(getUserId());
			if (isHas) {
				return pm.deleteMentorFromMentee(mentor, mentee);
			} else {
				return pm.deleteMentorFromMentee(mentee, mentor);
			}
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	@Override
	public void commment(String subject, String comment) {

		try {
			pm.createComment(new Feedback(getUserId(), subject, comment));
		} catch (MentorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<MentorsResult> getMentorAndMentees(ParticipantVO pi) {
		List<MentorsResult> result = new ArrayList<MentorsResult>();
		try {
			boolean isMentor = true; // for mentors
			Key key = KeyFactory.createKey(Participant.class.getSimpleName(), pi.getId());
			
			Participant m = pm.findById(key);
			List<Participant> participants = new ArrayList<Participant>();
			if(m.getMentors().size() != 0){
				participants = pm.getMentors(m);		
				for(Participant p: participants) {
					MentorsResult mentor= new MentorsResult(p.getName(),isMentor);
					result.add(mentor);
				}
				participants.clear();
			}
			if(m.getMentees().size() != 0) {
				isMentor = false; // for mentees
				participants = pm.getMentees(m);
				for(Participant p: participants) {
					MentorsResult mentor= new MentorsResult(p.getName(),isMentor);
					result.add(mentor);
				}
			}
			return result;
		} catch (MentorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public boolean deleteOpportunity(long id) {
		Key key = KeyFactory.createKey(Opportunity.class.getSimpleName(), id);
		return om.deleteOpportunity(key);
	}
	

	@Override
	public boolean addMentorToOpportunity(long id) {
		Key opportunityKey = KeyFactory.createKey(Opportunity.class.getSimpleName(), id);
		Participant mentor = null;
		try {
			mentor = pm.findParticipantByEmail(getUserId());
			if(om.addMentorToOpportunity(opportunityKey,mentor.getKey())){
				mentor.addMentoringOpportunities(opportunityKey);
				pm.save(mentor);
				return true;
			}
		} catch (MentorException e) {			
			e.printStackTrace();
		}		
		return false;
	}
	
	@Override
	public boolean removeMentorForOpportunity(long id) {
		Key opportunityKey = KeyFactory.createKey(Opportunity.class.getSimpleName(), id);
		Participant mentor = null;
		try {
			mentor = pm.findParticipantByEmail(getUserId());
			if(om.removeMentorFromOpportunity(opportunityKey,mentor.getKey())){
				mentor.removeMentoringOpportunity(opportunityKey);
				pm.save(mentor);
				return true;
			}
		} catch (MentorException e) {			
			e.printStackTrace();
		}		
		return false;
	}	
}
