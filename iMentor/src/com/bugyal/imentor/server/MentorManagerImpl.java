package com.bugyal.imentor.server;

import java.util.List;

import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.OpportunityManagerImpl;
import com.bugyal.imentor.server.data.Participant;
import com.bugyal.imentor.server.data.ParticipantManagerImpl;

public class MentorManagerImpl implements MentorManager {

	// TODO(raman): Add GUICE injection.. 
	ParticipantManager pManager;
	OpportunityManager oManager;
	
	MentorManagerImpl() {
		this.pManager = new ParticipantManagerImpl();
		this.oManager = new OpportunityManagerImpl();
	}
	
	@Override
	public OpportunityManager getOppurtunityManager() {
		return this.oManager;
	}

	@Override
	public ParticipantManager getParticipantManager() {
		return this.pManager;
	}

	@Override
	public List<Opportunity> getAllOppurtunities(Participant p) {
		return oManager.allOpportunites(p.getLoc());
	}

	@Override
	public List<Opportunity> getAllOppurtunities(Participant p, int radius) {
		Location l = p.getLoc();
		l.setActiveRadius(radius);
		
		return oManager.allOpportunities(l, p.getHasSubjects());
	}

	@Override
	public List<Opportunity> getAllOppurtunities(Participant p,
			List<String> subjects) {
		return oManager.allOpportunities(p.getLoc(), subjects);
	}

	@Override
	public List<Opportunity> getAllOppurtunities(Participant p,
			List<String> subjects, int radius) {
		Location l = p.getLoc();
		l.setActiveRadius(radius);
		
		return oManager.allOpportunities(l, subjects);
	}
}
