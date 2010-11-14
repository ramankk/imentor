package com.bugyal.imentor.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.server.OpportunityManager;
import com.bugyal.imentor.server.util.MyGeocellManager;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

public class OpportunityManagerImpl implements OpportunityManager {

	@Override
	public List<Opportunity> allOpportunities(Location location,
			List<String> subjects) {
		return search(location, subjects, false);
	}
	
	private List<Opportunity> search(Location l, List<String> subjects, boolean onlyActive) {
		Preconditions.checkNotNull(l);
		Preconditions.checkNotNull(subjects);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		List<Opportunity> results = null;
		
		Point center = new Point(l.getLatitude(), l.getLongitude());

		List<Object> params = new ArrayList<Object>();
		params.add(subjects);
		
		String filter = onlyActive ? "active == true && " : "";
		filter += "subjects.contains(subjectsP)";
		GeocellQuery baseQuery = new GeocellQuery(filter, "String subjectP", params);

		try {
			results = MyGeocellManager.proximityFetch(center, 30, l.getActiveRadius() * 1000, Opportunity.class, baseQuery, pm);
		} finally {
			pm.close();
		}
		
		return results;
	}
	
	private List<Opportunity> searchAll(Location l, boolean active) {
		Preconditions.checkNotNull(l);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		List<Opportunity> results = null;
		
		Point center = new Point(l.getLatitude(), l.getLongitude());

		List<Object> params = new ArrayList<Object>();
		params.add(new Boolean(true));
		
		String filter = active ? "active == activeP" : "";
		GeocellQuery baseQuery = new GeocellQuery(filter, "Boolean activeP", params);

		try {
			results = MyGeocellManager.proximityFetch(center, 30, l.getActiveRadius() * 1000, Opportunity.class, baseQuery, pm);
		} finally {
			pm.close();
		}
		
		return results;
	}

	@Override
	public Opportunity createOpportunity(Location location,
			List<String> subjects, int requiredMentors,
			List<Participant> contacts, int priority) {
		Opportunity o = new Opportunity(location, subjects, requiredMentors, contacts, priority);
		save(o);
		return o;
	}
	
	@Override
	public void save(Opportunity... oppurtunities) {
		System.out.println("Trying to save :: " + oppurtunities);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			pm.makePersistentAll(oppurtunities);
		} finally {
			pm.close();
		}
	}

	@Override
	public List<Opportunity> searchOpportunities(Location location,
			List<String> subjects) {
		return search(location, subjects, true);
	}

	@Override
	public List<Opportunity> allOpportunites(Location location) {
		dumpAll();
		
		return searchAll(location, true);
	}

	private void dumpAll() {
		
	}
}
