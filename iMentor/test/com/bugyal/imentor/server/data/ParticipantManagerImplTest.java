package com.bugyal.imentor.server.data;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bugyal.imentor.MentorException;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.junit.Assert.*;

public class ParticipantManagerImplTest {

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
	public void testSaveAndSearch() throws Exception {
		ParticipantManagerImpl pmi = new ParticipantManagerImpl();
		
		Participant p = pmi.createParticipant("Raman", getDummyLocation(), "raman@gmail.com");
		p.addInterest("interest1");
		pmi.save(p);
		
		Participant pd = pmi.createParticipant("Kumar", getDummyLocation(), p);
		pd.addInterest("interest1");
		pmi.save(pd);
		
		System.out.println("Created participant with key .. " + p.getKey());

		List<Participant> pList = pmi.searchParticipantsByInterest("interest1");
		
		assertEquals(2, pList.size());
		assertEquals(p, pList.get(0));
		assertEquals(pd, pList.get(1));
	}
	
	@Test
	public void testMultipleParticipantsWithSameEmail() throws Exception {
		ParticipantManagerImpl pmi = new ParticipantManagerImpl();
		
		pmi.createParticipant("Raman", getDummyLocation(), "raman@gmail.com");
		try {
			pmi.createParticipant("Kumar", getDummyLocation(), "raman@gmail.com");
			fail();
		} catch (MentorException me) {
			assertEquals("Participant with email raman@gmail.com already exists." , me.getMessage());
		}
	}
	
	@Test
	public void testMentorsMentees() throws Exception {
		ParticipantManagerImpl pmi = new ParticipantManagerImpl();
		
		Participant p = pmi.createParticipant("Raman", getDummyLocation(), "raman@bugyal.com");
		Participant p2 = pmi.createParticipant("Kumar", getDummyLocation(), "kumar@bugyal.com");
		
		Participant m1 = pmi.createParticipant("mentee1", getDummyLocation(), p2);
		pmi.addMentor(m1, p);
		
		List<Participant> mList = pmi.getMentors(m1);
		assertEquals(1, mList.size());
		assertEquals(p, mList.get(0));
		
		mList = pmi.getMentees(p);
		assertEquals(1, mList.size());
		assertEquals(m1, mList.get(0));
	}
	
	@Test
	public void testKnowledgeQueries() throws Exception {
		ParticipantManagerImpl pmi = new ParticipantManagerImpl();
		
		Participant p = pmi.createParticipant("Raman", getDummyLocation(), "raman@bugyal.com");
		Participant p2 = pmi.createParticipant("Kumar", getDummyLocation(), "kumar@bugyal.com");
		
		printCells(p);
		printCells(p2);
		
		pmi.addHasKnowledge(p, "Math", 5, p);
		pmi.addHasKnowledge(p, "CS", 6, p);
		pmi.addHasKnowledge(p2, "Math", 3, p);
		pmi.addHasKnowledge(p2, "Photography", 5, p2);
		
		pmi.addNeedKnowledge(p2, "CS", 4, p2);
		pmi.addNeedKnowledge(p, "Photography", 3, p);
		
		List<Participant> pl = pmi.searchParticipantsBySubject("CS", getDummyLocation(), true);
		assertEquals(1, pl.size());
		assertEquals(p, pl.get(0));
		
		pl = pmi.searchParticipantsBySubject("Math", getDummyLocation(), true);
		assertEquals(2, pl.size());
		assertEquals(p, pl.get(0));
		assertEquals(p2, pl.get(1));
		
		pl = pmi.searchParticipantsBySubject("Photography", getDummyLocation(), false);
		assertEquals(1, pl.size());
		assertEquals(p, pl.get(0));
		
		pl = pmi.searchParticipantsBySubject("Photography", new Location(17.01111, 78.5899, "test", 100), false);
		assertEquals(1, pl.size());
		assertEquals(p, pl.get(0));
		
		pl = pmi.searchParticipantsBySubject("Photography", new Location(15.01111, 78.5899, "test", 100), false);
		assertEquals(0, pl.size());
		
		pl = pmi.searchParticipantsBySubject("Photography", new Location(17.01111, 77.5199, "test", 100), false);
		assertEquals(0, pl.size());
	}

	private void printCells(Participant p) {
		System.out.println(p.getName() + " geocells -- ");
		for (String cell : p.getGeocells()) {
			System.out.println("----------   " + cell);
		}
	}

	private Location getDummyLocation() {
		return new Location(17.442945, 78.353333, "Laxmi Enclace, Gachibowli, hyderabad", 10);
	}
}

