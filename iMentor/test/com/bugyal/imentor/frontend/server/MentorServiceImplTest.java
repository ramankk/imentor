package com.bugyal.imentor.frontend.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.server.TestLocations;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class MentorServiceImplTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testSomeThing() throws Exception {
		MentorServiceImpl msi = new MentorServiceImpl();

		List<String> subjects = new ArrayList<String>();
		subjects.add("CS");
		subjects.add("Math");

		ParticipantVO pvo = msi.create(new ParticipantVO(null, "raman", "12903138",
				TestLocations.GACHIBOWLI.getLatitude(),
				TestLocations.GACHIBOWLI.getLongitude(),
				TestLocations.GACHIBOWLI.getLocationString(), 
				10, subjects, subjects));
		
		assertNotNull(pvo.getId());
		assertTrue(pvo.getId() > 0);
		
		msi.createOpportunity(new OpportunityVO(null, subjects, 2, 6, TestLocations.GACHIBOWLI.getLatitude(),
				TestLocations.GACHIBOWLI.getLongitude(), 10,
				TestLocations.GACHIBOWLI.getLocationString()));
		
		List<OpportunityVO> opportunities = msi.find(subjects, pvo);
		
		assertEquals(1, opportunities.size());
		assertEquals(6, opportunities.get(0).getPriority());
	}
}
