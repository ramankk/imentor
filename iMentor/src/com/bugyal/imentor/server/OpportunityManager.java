package com.bugyal.imentor.server;

import java.util.List;

import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;
import com.google.appengine.api.datastore.Key;

public interface OpportunityManager {

	// searches active oppurtunities.
	public List<Opportunity> searchOpportunities(Location location,
			List<String> subjects);

	// list all oppurtunities
	public List<Opportunity> allOpportunities(Location location,
			List<String> subjects);
	
	public List<Opportunity> allOpportunites(Location location);
	
	public void save(Opportunity... oppurtunities);

	long deleteOpportunities();

	public List<Opportunity> searchOpportunitiesByKey(Key key);

	public Opportunity createOpportunity(Location location, List<String> subjects,
			int requiredMentors, List<Participant> contacts,
			int priority, String message, Participant savedBy);

	public Opportunity findById(Key key);

	void update(Opportunity opportunity, Participant savedBy);

	boolean deleteOpportunity(Key key);

	boolean addMentorToOpportunity(Key oppKey, Key mentorKey);
}
