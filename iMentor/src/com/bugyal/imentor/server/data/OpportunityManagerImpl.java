package com.bugyal.imentor.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.frontend.server.StatsServlet;
import com.bugyal.imentor.frontend.server.StatsServlet.AverageStat;
import com.bugyal.imentor.server.OpportunityManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

public class OpportunityManagerImpl implements OpportunityManager {

	static AverageStat allOpportunitiesByLocationAndSubjectsTimeState = StatsServlet.createAverageStat("allOpportunitiesByLocationAndSubjects_total_time");
	@Override
	public List<Opportunity> allOpportunities(Location location,
			List<String> subjects) {
		long t = System.currentTimeMillis();
		List<Opportunity> result = search(location, subjects, false);
		allOpportunitiesByLocationAndSubjectsTimeState.inc(System.currentTimeMillis() - t);
		return result;		
	}
	
	static AverageStat deleteOpportunityTimeState = StatsServlet.createAverageStat("deleteOpportunity_total_time");
	@Override
	public boolean deleteOpportunity(Key key) {
		long t = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean status;
		try{
			Opportunity o =  pm.getObjectById(Opportunity.class, key);
			tx = pm.currentTransaction();
			tx.begin();
			pm.deletePersistent(o);
			tx.commit();
			status = true;
		} 
		catch (Exception e)
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }
		    status = false;
		}
		deleteOpportunityTimeState.inc(System.currentTimeMillis() - t);
		return status;
	}
	
	static AverageStat deleteOpportunitiesTimeState = StatsServlet.createAverageStat("deleteOpportunities_total_time");
	@Override
	public long deleteOpportunities() {

		long t = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long n;
		try {
			Query q = pm.newQuery(Opportunity.class);
			n = q.deletePersistentAll();
			q.closeAll();

		} finally {
			pm.close();
		}

		 deleteOpportunitiesTimeState.inc(System.currentTimeMillis() - t);
		return n;
	}

	private List<Opportunity> search(Location l, List<String> subjects,
			boolean onlyActive) {
		Preconditions.checkNotNull(l);
		Preconditions.checkNotNull(subjects);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Opportunity> results = null;

		Point center = new Point(l.getLatitude(), l.getLongitude());

		List<Object> params = new ArrayList<Object>();
		params.add(subjects);

		String filter = onlyActive ? "active == true && " : "";
		filter += "subjects.contains(subjectsP)";

		GeocellQuery baseQuery = new GeocellQuery(filter, "String subjectsP",
				params);

		try {
			long t = System.currentTimeMillis();
			System.out.println(t);
			results = GeocellManager.proximityFetch(center, 30,
					l.getActiveRadius() * 1000, Opportunity.class, baseQuery,
					pm, 8);
			System.out.println("opportunity search "
					+ (System.currentTimeMillis() - t));
		} finally {
			pm.close();
		}
		return results;
	}

	private List<Opportunity> searchAll(Location l, boolean active) {

		Preconditions.checkNotNull(l);
		long CHECK_TIME = 24 * (60 * 60 * 1000);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Opportunity> results = null;
		long checkTime = System.currentTimeMillis() - CHECK_TIME;

		Point center = new Point(l.getLatitude(), l.getLongitude());

		List<Object> params = new ArrayList<Object>();
		params.add(checkTime);

		String filter = active ? "active == true " : "";
		filter += "&& lastModifiedTime >= updateTimeP";
		GeocellQuery baseQuery = new GeocellQuery(filter, "long updateTimeP",
				params);

		try {
			long t = System.currentTimeMillis();
			System.out.println(t);
			results = GeocellManager.proximityFetch(center, 60,
					l.getActiveRadius() * 1000, Opportunity.class, baseQuery,
					pm);
			System.out.println("opportunity search all "
					+ (System.currentTimeMillis() - t));
		} finally {
			pm.close();
		}

		return results;
	}

	
	@Override
	public Opportunity createOpportunity(Location location,
			List<String> subjects, int requiredMentors,
			List<Participant> contacts, int priority, String message, Participant savedBy) {

		Opportunity o = new Opportunity(location, subjects, requiredMentors,
				contacts, priority, message, savedBy);
		save(o);		
		return o;
	}
	
	static AverageStat saveOpportunityTimeState = StatsServlet.createAverageStat("saveOpportunity_total_time");
	@Override
	public void save(Opportunity... oppurtunities) {
		long t = System.currentTimeMillis();
		System.out.println("Trying to save :: " + oppurtunities);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			pm.makePersistentAll(oppurtunities);
		} finally {
			pm.close();
		}
		saveOpportunityTimeState.inc(System.currentTimeMillis() - t);
	}
	
	static AverageStat updateOpportunitiesTimeState = StatsServlet.createAverageStat("updateOpportunities_total_time");
	@Override
	public Opportunity update(Opportunity opportunity, Participant savedBy){

		long t = System.currentTimeMillis();
		System.out.println("Trying to save :: " + opportunity);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try
		{
		    tx.begin();
		    Opportunity vo = pm.getObjectById(Opportunity.class, opportunity.getKey());
		    vo.setLocation(opportunity.getLoc());
		    vo.resetSubjects(opportunity.getSubjects(), savedBy);
		    tx.commit();
		    updateOpportunitiesTimeState.inc(System.currentTimeMillis() - t);
			return vo;	
		}
		catch (Exception e)
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }
		}
		updateOpportunitiesTimeState.inc(System.currentTimeMillis() - t);
		return null;		
	}

	static AverageStat searchOpportunitiesTimeState = StatsServlet.createAverageStat("searchOpportunities_total_time");
	@Override
	public List<Opportunity> searchOpportunities(Location location,
			List<String> subjects) {

		long t = System.currentTimeMillis();
		List<Opportunity> result = search(location, subjects, true);

		searchOpportunitiesTimeState.inc(System.currentTimeMillis() - t);
		return result;
	}
	static AverageStat allOpportunitiesByLocationTimeState = StatsServlet.createAverageStat("allOpportunitiesByLocation_total_time");
	@Override
	public List<Opportunity> allOpportunites(Location location) {
		long t = System.currentTimeMillis();
		List<Opportunity> result = searchAll(location, true);

		allOpportunitiesByLocationTimeState.inc(System.currentTimeMillis() - t);
		return result;
	}
	static AverageStat searchOpportunitiesByKeyTimeState = StatsServlet.createAverageStat("searchOpportunitiesByKey_total_time");
	@SuppressWarnings("unchecked")
	@Override
	public List<Opportunity> searchOpportunitiesByKey(Key key) {

		long t = System.currentTimeMillis();
		Preconditions.checkNotNull(key);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Opportunity> results = null;

		String filter = "contacts.contains(keyP)";
		Query q = pm.newQuery(Opportunity.class, filter);
		q.declareParameters(Key.class.getName() + " keyP");

		results = (List<Opportunity>) q.execute(key);
		searchOpportunitiesByKeyTimeState.inc(System.currentTimeMillis() - t);
		return results;
	}
	
	static AverageStat findByIdTimeState = StatsServlet.createAverageStat("findByIdOpportunity_total_time");
	@Override
	public Opportunity findById(Key key) {

		long t = System.currentTimeMillis();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Opportunity o = null;
		try {
			o = pm.getObjectById(Opportunity.class, key);
			// Magic to load knowledge and notes.
			o.getChangeInfo();
			o.getContacts();
		} finally {
			pm.close();
		}
		findByIdTimeState.inc(System.currentTimeMillis() - t);
		return o;
	}
		
	static AverageStat addMentorToOpportunityState = StatsServlet.createAverageStat("addMentorToOpportunity_total_time");
	@Override
	public boolean addMentorToOpportunity(Key oppKey, Key mentorKey){
		long t = System.currentTimeMillis();
		boolean status;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
		    tx.begin();
		    Opportunity vo = pm.getObjectById(Opportunity.class, oppKey);
		    vo.addMentor(mentorKey);
		    tx.commit();
		    status = true;
		}
		catch (Exception e)
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }
		    status = false;
		}		
		addMentorToOpportunityState.inc(System.currentTimeMillis() - t);
		return status;
	}
	
	static AverageStat removeMentorFromOpportunityState = StatsServlet.createAverageStat("removeMentorFromOpportunity_total_time");
	@Override
	public boolean removeMentorFromOpportunity(Key oppKey, Key mentorKey) {
		long t = System.currentTimeMillis();
		boolean status;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
		    tx.begin();
		    Opportunity vo = pm.getObjectById(Opportunity.class, oppKey);
		    vo.removeMentor(mentorKey);
		    tx.commit();
		    status = true;
		}
		catch (Exception e)
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }
		    status = false;
		}		
		removeMentorFromOpportunityState.inc(System.currentTimeMillis() - t);
		return status;
	}
}
