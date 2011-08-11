package com.bugyal.imentor.server;

import java.util.List;

import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;


public interface MentorManager {
	
	public final static MentorManager INSTANCE = new MentorManagerImpl();
	
	public ParticipantManager getParticipantManager();
	
	public OpportunityManager getOpportunityManager();
	
	public List<Opportunity> getAllOppurtunities(Participant p);
	
	public List<Opportunity> getAllOppurtunities(Participant p, int radius);

	public List<Opportunity> getAllOppurtunities(Participant p, List<String> subjects);
	
	public List<Opportunity> getAllOppurtunities(Participant p, List<String> subjects, int radius);
}
