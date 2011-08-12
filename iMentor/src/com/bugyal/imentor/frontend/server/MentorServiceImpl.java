package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bugyal.imentor.MentorException;
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
		Participant p = MentorManager.INSTANCE.getParticipantManager()
				.findById(key);

		List<OpportunityVO> rList = new ArrayList<OpportunityVO>();

		List<Opportunity> oList = null;
		if (subjects != null) {
			oList = MentorManager.INSTANCE.getAllOppurtunities(p, subjects);
		} else {
			oList = MentorManager.INSTANCE.getAllOppurtunities(p);
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
			ParticipantManager participantManager = MentorManager.INSTANCE.getParticipantManager();
			pi = participantManager
					.createParticipant(p.getName(), location, p.getEmail());
			for (String subject : p.getNeedSubjects()) {
			  participantManager.addNeedKnowledge(pi, subject, 1, pi);
			} 
			for (String subject : p.getHasSubjects()) {
			  participantManager.addHasKnowledge(pi, subject, 1, pi);
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
				MentorManager.INSTANCE.getParticipantManager().addHasKnowledge(
						pi, has, 5, pi);
			}
		}

		if (p.getNeedSubjects() != null) {
			for (String need : p.getNeedSubjects()) {
				MentorManager.INSTANCE.getParticipantManager()
						.addNeedKnowledge(pi, need, 5, pi);
			}
		}

		pi.setLocation(location);
		MentorManager.INSTANCE.getParticipantManager().save(pi);
	}

	@Override
	public OpportunityVO createOpportunity(OpportunityVO o) throws MeException {
		if (o.getId() != null) {
			throw new MeException("Cannot create already created participant");
		}

		Location location = new Location(o.getLatitude(), o.getLongitude(), o
				.getLocString(), o.getRadius());
		Opportunity oi = null;

		// TODO(raman): Understand why MentorException is not getting thrown.
		OpportunityManager om = MentorManager.INSTANCE.getOpportunityManager();
		oi = om.createOpportunity(location, o.getSubjects(), o
				.getRequiredMentors(), null, o.getPriority());

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
			pi = MentorManager.INSTANCE.getParticipantManager().findById(key);
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
		throw new MeException("Unimplemented !! - fix me");
	}

	@Override
	public List<String> getSubjects() throws MeException {
		List<String> list = new ArrayList<String>();

		Subject sub[] = Subject.values();
		
		for(Subject s : sub)
		{
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
	public SearchResponse feedToMe(String emailId) throws MeException {
		SearchResponse response = new SearchResponse();
		try {
			long t=System.currentTimeMillis();
			System.out.println(t);
			Participant pi = getParticipant(emailId);
			
			System.out.println("part:" + (System.currentTimeMillis()-t));
			return filterList(pi.getLocation().getLat(), pi.getLocation().getLon(), pi.getLoc().getLocationString(), pi.getLoc().getActiveRadius(), pi.getHasSubjects(), pi.getNeedSubjects());

		} catch (MentorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private Participant getParticipant(String emailId) throws MentorException {
		return MentorManager.INSTANCE.getParticipantManager().findParticipantByEmail(emailId);
	}

	@Override
	public long deleteRecords(){
		long m = 0;
		long n = 0;
		ParticipantManager pm = MentorManager.INSTANCE.getParticipantManager();
		OpportunityManager om = MentorManager.INSTANCE.getOpportunityManager();
		try{
			m = pm.deleteParticipants();
			n = om.deleteOpportunities();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m+n;
	}
	
	@Override
	public SearchResponse filterList(double latitude, double longitude,
			String strlocation, int radius, List<String> hasSubs,
			List<String> needSubs) {
		SearchResponse response = new SearchResponse();
		try{
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
			
			Location location = new Location(latitude, longitude, strlocation, radius);
			
			ParticipantManager pm = MentorManager.INSTANCE.getParticipantManager();
			
			for (Participant p : pm.searchParticipantsBySubjects(needSubs, location, true)) {
				List<String> matchingSubs = new ArrayList<String>();
				for (String s : p.getHasSubjects()) {
					if (needSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				has.add(new SearchResult(ValueObjectGenerator.create(p), true, matchingSubs));
			}

			for (Participant p : pm.searchParticipantsBySubjects(hasSubs, location, false)) {
				List<String> matchingSubs = new ArrayList<String>();
				for (String s : p.getNeedSubjects()) {
					if (hasSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				need.add(new SearchResult(ValueObjectGenerator.create(p), false, matchingSubs));
			}
			
			for (Opportunity o : MentorManager.INSTANCE.getOpportunityManager().searchOpportunities(location, hasSubs)) {
				List<String> matchingSubs = new ArrayList<String>();
				for (String s : o.getSubjects()) {
					if (hasSubjects.contains(s)) {
						matchingSubs.add(s);
					}
				}
				need.add(new SearchResult(ValueObjectGenerator.create(o), matchingSubs));
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
	public SearchResponse localActivity(String emailId) {
		SearchResponse response = new SearchResponse();
		try{
			List<SearchResult> has = new ArrayList<SearchResult>();
			List<SearchResult> need = new ArrayList<SearchResult>();
			
			Participant pi = getParticipant(emailId);
			Location location = new Location(pi.getLocation().getLat(),  pi.getLocation().getLon(), pi.getLoc().getLocationString(), pi.getLoc().getActiveRadius());
			
			ParticipantManager pm = MentorManager.INSTANCE.getParticipantManager();
			
			
			for (Participant p : pm.searchParticipantsByLocation(location)) {
				has.add(new SearchResult(ValueObjectGenerator.create(p), true, p.getHasSubjects()));
				need.add(new SearchResult(ValueObjectGenerator.create(p), false, p.getNeedSubjects()));
			}
			
			for (Opportunity o : MentorManager.INSTANCE.getOpportunityManager().allOpportunites(location)) {
				need.add(new SearchResult(ValueObjectGenerator.create(o),o.getSubjects()));
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
	public List<OpportunityVO> getOpportunitiesById(String emailId) {
		Participant p = null;
		try{
			p = getParticipant(emailId);
			OpportunityManager om = MentorManager.INSTANCE.getOpportunityManager();
			return ValueObjectGenerator.createOpportunityVOs(om.searchOpportunitiesByKey(p.getKey()));
		}catch(MentorException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
}
