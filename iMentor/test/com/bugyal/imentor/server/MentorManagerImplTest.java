package com.bugyal.imentor.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


public class MentorManagerImplTest {
	
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
	public void testFindOppurtunities() throws Exception {
		MentorManagerImpl mmi = new MentorManagerImpl();
		
		ParticipantManager pmi = mmi.getParticipantManager();
		OpportunityManager omi = mmi.getOpportunityManager();
		
		List<String> subjects = new ArrayList<String>();
		subjects.add("Math");
		subjects.add("CS");
		
		List<String> ss = new ArrayList<String>();
		ss.add("CS");
		
		Participant p = pmi.createParticipant("raman","male", TestLocations.GACHIBOWLI, "raman@bugyal.com", "100002992300278");
		List<Participant> plist = new ArrayList<Participant>();
		plist.add(p);
		
		Participant seaker = pmi.createParticipant("kumar", "male", TestLocations.BODUPPAL_10, "kumar@bugyal.com", "100002992300278");
		pmi.addHasKnowledge(seaker, Arrays.asList(new String[]{"CS"}), 6, seaker);
		
		Participant seaker2 = pmi.createParticipant("krk", "female", TestLocations.KONDAPUR, "krk@bugyal.com", "100002992300278");
		pmi.addHasKnowledge(seaker2, Arrays.asList(new String[]{"Math"}), 4, seaker2);
		
		Opportunity o1 = omi.createOpportunity(TestLocations.KPHB, subjects, 2, plist, 7, "", p);
		Opportunity o2 = omi.createOpportunity(TestLocations.KONDAPUR, ss, 4, plist, 6, "", p);
		Opportunity o3 = omi.createOpportunity(TestLocations.YUKSOM, subjects, 4, plist, 6, "", p);
		
		List<Opportunity> oList = mmi.getAllOppurtunities(seaker2);
		assertEquals(1, oList.size());
		assertEquals(o1, oList.get(0));
		
		oList = mmi.getAllOppurtunities(seaker);
		assertEquals(0, oList.size());
		
		oList = mmi.getAllOppurtunities(seaker, 30);
		assertEquals(2, oList.size());
		assertTrue(oList.contains(o1));
		assertTrue(oList.contains(o2));
		
		oList = mmi.getAllOppurtunities(seaker, 2000);
		assertEquals(3, oList.size());
		assertTrue(oList.contains(o1));
		assertTrue(oList.contains(o2));
		assertTrue(oList.contains(o3));
	}

}
