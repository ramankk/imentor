package com.bugyal.imentor.server.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bugyal.imentor.server.TestLocations;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


public class OpportunityManagerImplTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Before
	public void setUp() {
		helper.setUp();
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	@Test
	public void testCreateAndSearch() throws Exception {
		ParticipantManagerImpl pmi = new ParticipantManagerImpl();
		OpportunityManagerImpl omi = new OpportunityManagerImpl();
		
		List<String> subjects = new ArrayList<String>();
		subjects.add("Math");
		subjects.add("CS");
		
		Participant p = pmi.createParticipant("raman", "male", TestLocations.GACHIBOWLI, "raman@bugyal.com", "100002992300278");
		List<Participant> plist = new ArrayList<Participant>();
		plist.add(p);
		
		Opportunity o1 = omi.createOpportunity(TestLocations.KPHB, subjects, 2, plist, 7, "", p);
		
		List<String> ss = new ArrayList<String>();
		ss.add("CS");
		
		List<Opportunity> oList = omi.searchOpportunities(TestLocations.GACHIBOWLI, ss);
		assertEquals(1, oList.size());
		assertEquals(o1, oList.get(0));
		
		oList = omi.searchOpportunities(TestLocations.BODUPPAL_10, ss);
		assertEquals(0, oList.size());
		
		oList = omi.searchOpportunities(TestLocations.BODUPPAL_25, ss);
		assertEquals(1, oList.size());
		assertEquals(o1, oList.get(0));
	}

}
