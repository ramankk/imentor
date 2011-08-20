package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.frontend.client.LocationData;
import com.bugyal.imentor.frontend.client.MentorService;
import com.bugyal.imentor.frontend.shared.MeException;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.MentorManager;
import com.bugyal.imentor.server.OpportunityManager;
import com.bugyal.imentor.server.ParticipantManager;
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
	private static final ParticipantManager pm = MentorManager.INSTANCE.getParticipantManager();
	private static final OpportunityManager om = MentorManager.INSTANCE.getOpportunityManager();
	private static final MentorManager mm = MentorManager.INSTANCE;
	
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
		if (p.getId() != null) {
			throw new MeException("Cannot create already created participant");
		}

		Location location = new Location(p.getLatitude(), p.getLongitude(), p
				.getLocationString(), p.getRadius());
		Participant pi = null;
		try {
			pi = pm.createParticipant(p.getName(), p.getGender(), location, p.getEmail());
			for (String subject : p.getNeedSubjects()) {
			  pm.addNeedKnowledge(pi, subject, 1, pi);
			} 
			for (String subject : p.getHasSubjects()) {
			  pm.addHasKnowledge(pi, subject, 1, pi);
			}
			
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

		if (p.getHasSubjects() != null) {
			for (String has : p.getHasSubjects()) {
				pm.addHasKnowledge(pi, has, 5, pi);
			}
		}

		if (p.getNeedSubjects() != null) {
			for (String need : p.getNeedSubjects()) {
				pm.addNeedKnowledge(pi, need, 5, pi);
			}
		}

		pi.setLocation(location);
		pm.save(pi);
	}
	
	private void save(Opportunity oi, OpportunityVO o, String emailId) throws MentorException {
		Location location = new Location(o.getLatitude(), o.getLongitude(),
				o.getLocString(), o.getRadius());
		oi.setLocation(location);		
		Participant savedBy = pm.findParticipantByEmail(emailId);
		
		if(o.getSubjects() != null) {
			oi.resetSubjects(o.getSubjects(), savedBy);	
		}
		
		oi.setPriority(o.getPriority(), savedBy);
		oi.setActive(true, savedBy);
		oi.setRequiredParticipants(o.getRequiredMentors(), savedBy);
 		om.update(oi, savedBy);
	}

	@Override
	
	public OpportunityVO createOpportunity(String emailId, OpportunityVO o) throws MeException {
		if (o.getId() != null) {
			throw new MeException("Cannot create already created participant");
		}

		Location location = new Location(o.getLatitude(), o.getLongitude(), o
				.getLocString(), o.getRadius());
		Opportunity oi = null;
		List<Participant> contacts = new ArrayList<Participant>();
		
		Participant participant=null;
		try {
			participant = pm.findParticipantByEmail(emailId);
			if (participant != null) {
			  contacts.add(participant);
			}
		} catch (MentorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// TODO(raman): Understand why MentorException is not getting thrown.
		oi = om.createOpportunity(location, o.getSubjects(), o
				.getRequiredMentors(), contacts, o.getPriority(), o.getMessage(), participant);

		if (oi != null) {
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
	public OpportunityVO updateOpportunity(OpportunityVO o, String emailId) throws MeException {
		if (o.getId() == null) {
			throw new MeException("New opportunity, cannot update.");
		}

		Opportunity oi = null;
		Key key = KeyFactory.createKey(Opportunity.class.getSimpleName(), o.getId());

		try {
			oi = om.findById(key);
			save(oi, o, emailId);
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
						matchingSubs));
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
						false, matchingSubs));
			}

			for (Opportunity o : om.searchOpportunities(location, hasSubs)) {

				List<String> matchingSubs = new ArrayList<String>();
				for (String s : o.getSubjects()) {
					if (hasSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				need.add(new SearchResult(ValueObjectGenerator.create(o),
						matchingSubs));
			}
			response.setHas(has);
			response.setNeed(need);
			return response;
		} catch (MentorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	
	@Override
	public SearchResponse feedToMe(String emailId) throws MeException {
		SearchResponse response = new SearchResponse();
		try {
			Participant pi = pm.findParticipantByEmail(emailId);

			response = filterList(pi.getLocation().getLat(), pi.getLocation()
					.getLon(), pi.getLoc().getLocationString(), pi.getLoc()
					.getActiveRadius(), pi.getHasSubjects(), pi
					.getNeedSubjects());

			feedRanker.rank(response, pi);
			return response;
		} catch (MentorException e) {
			e.printStackTrace();
		}

		return response;
	}

	public ParticipantVO getParticipantVOByEmailId(String emailId)
			throws MeException {
		try {
			return ValueObjectGenerator.create(pm.findParticipantByEmail(emailId));
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	public SearchResponse localActivity(String emailId) {
		SearchResponse response = new SearchResponse();
		try {
			List<SearchResult> has = new ArrayList<SearchResult>();
			List<SearchResult> need = new ArrayList<SearchResult>();

			Participant pi = pm.findParticipantByEmail(emailId);
			Location location = new Location(pi.getLocation().getLat(), pi
					.getLocation().getLon(), pi.getLoc().getLocationString(),
					pi.getLoc().getActiveRadius());

			for (Participant p : pm.searchParticipantsByLocation(location)) {
				has.add(new SearchResult(ValueObjectGenerator.create(p), true,
						p.getHasSubjects()));
				need.add(new SearchResult(ValueObjectGenerator.create(p),
						false, p.getNeedSubjects()));
			}

			for (Opportunity o : om.allOpportunites(location)) {
				need.add(new SearchResult(ValueObjectGenerator.create(o), o
						.getSubjects()));
			}
			response.setHas(has);
			response.setNeed(need);
			return response;
		} catch (MentorException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<OpportunityVO> getOpportunitiesById(String emailId) {
		Participant p = null;
		try {
			p = pm.findParticipantByEmail(emailId);
			return ValueObjectGenerator.createOpportunityVOs(om.searchOpportunitiesByKey(p.getKey()));
		} catch(MentorException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void createSession(String emailId) throws MeException {
		// HttpServletRequest request = this.getThreadLocalRequest();
		// HTTPSession session = request.getSession();
		// session.setAttribute("Username", Username);

		// same as above
		getThreadLocalRequest().getSession().setAttribute("UserId", emailId);

	}

	@Override
	public boolean validateSession(String emailId) throws MeException {
		if (getThreadLocalRequest().getSession().getAttribute("UserId") != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteSession(String emailId) throws MeException {
		if (getThreadLocalRequest().getSession().getAttribute("Username") != null) {
			getThreadLocalRequest().getSession().removeAttribute("UserId");
			return true;
		} else {
			return false;
		}
	}


}
