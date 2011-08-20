package com.bugyal.imentor.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.server.OpportunityManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

public class OpportunityManagerImpl implements OpportunityManager {

	@Override
	public List<Opportunity> allOpportunities(Location location,
			List<String> subjects) {
		return search(location, subjects, false);
	}

	@Override
	public long deleteOpportunities() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		long n;
		try {
			Query q = pm.newQuery(Opportunity.class);
			n = q.deletePersistentAll();
			q.closeAll();

		} finally {
			pm.close();
		}
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
	public void update(Opportunity opportunity, Participant savedBy){
		
		System.out.println("Trying to save :: " + opportunity);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try
		{
		    tx.begin();
		    Opportunity vo = pm.getObjectById(Opportunity.class, opportunity.getKey());
		    vo.setLocation(opportunity.getLoc());
		    vo.resetSubjects(opportunity.getSubjects(), savedBy);
		    /*
		    for(String sub: opportunity.getSubjects()){
		    	vo.addSubject(sub, savedBy);
		    }*/
		    tx.commit();
		}
		catch (Exception e)
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }
		}
		
		
	}

	@Override
	public List<Opportunity> searchOpportunities(Location location,
			List<String> subjects) {
		return search(location, subjects, true);
	}

	@Override
	public List<Opportunity> allOpportunites(Location location) {
		return searchAll(location, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Opportunity> searchOpportunitiesByKey(Key key) {
		Preconditions.checkNotNull(key);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Opportunity> results = null;

		String filter = "contacts.contains(keyP)";
		Query q = pm.newQuery(Opportunity.class, filter);
		q.declareParameters(Key.class.getName() + " keyP");

		results = (List<Opportunity>) q.execute(key);
		return results;
	}

	/*public Opportunity searchOpportunityByKey(String key) {
		Preconditions.checkNotNull(key);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		
		 * Object result =null; String filter = "key.contains(keyP)"; Query q =
		 * pm.newQuery(Opportunity.class, filter);
		 * q.declareParameters("Object keyp"); result = q.execute(key);
		 
		Key k = null;
		try {
			k = KeyFactory.stringToKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Opportunity result = pm.getObjectById(Opportunity.class, "749");
		return result;
	}*/

	@Override
	public Opportunity findById(Key key) {
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

		return o;
	}
}
