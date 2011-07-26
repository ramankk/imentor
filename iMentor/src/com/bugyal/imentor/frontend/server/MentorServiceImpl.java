package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.MentorException;
import com.bugyal.imentor.frontend.client.MentorService;
import com.bugyal.imentor.frontend.shared.MeException;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
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
		OpportunityManager om = MentorManager.INSTANCE.getOppurtunityManager();
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
	public void generateRandomData() throws MeException {
		try {
			new DataGenerator(1000);
		} catch (MentorException e) {
			e.printStackTrace();
			throw new MeException(e.getMessage());
		}
	}

}
