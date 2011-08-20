package com.bugyal.imentor.frontend.server;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.data.Location;
import com.bugyal.imentor.server.data.Participant;

public class RankerTest {

	SearchResponse sr1 = new SearchResponse();
	ParticipantVO p1, p2, p3, p4, p5;

	@Before
	public void setup() {
		p1 = new ParticipantVO(null, "name1", "m", "name1@k.c", 17.000000, 75.0, "",
				200000, Arrays.asList("chemistry", "physics"), Arrays.asList(
						"maths", "science"));

		p2 = new ParticipantVO(null, "name2", "m", "name2@k.c", 17.000000, 75.1, "",
				200000, Arrays.asList("maths"), Arrays.asList("chemistry",
						"physics"));

		p3 = new ParticipantVO(null, "name3", "m", "name3@k.c", 17.000000, 75.2, "",
				200000, Arrays.asList("maths", "science"), Arrays
						.asList("chemistry"));

		p4 = new ParticipantVO(null, "name4", "m", "name4@k.c", 17.000000, 75.3, "",
				200000, Arrays.asList("maths"), Arrays.asList("physics"));

		p5 = new ParticipantVO(null, "name5", "m", "name5@k.c", 17.000000, 75.4, "",
				200000, Arrays.asList("science"), Arrays.asList("chemistry",
						"physics"));

		sr1.add(new SearchResult(p1, true, Arrays.asList("maths", "science")));

		sr1.add(new SearchResult(p2, false, Arrays.asList("maths")));
		sr1.add(new SearchResult(p3, false, Arrays.asList("maths", "science")));
		sr1.add(new SearchResult(p4, false, Arrays.asList("maths")));
		sr1.add(new SearchResult(p5, false, Arrays.asList("science")));
	}

	@After
	public void cleanup() {

	}

	@Test
	public void testRankedCorrectly() {

		Ranker disSubRanker = new Ranker().addScorer(new DistanceScorer())
				.addScorer(new SubjectCorrelationScorer());

		Participant p = Participant.createParticipantForTest("abc",
				new Location(17.0, 75.12, "some-location", 40), "abc@ui.com");
		disSubRanker.rank(sr1, p) ;
		
		assertEquals("name2",sr1.getAllResults().get(0).getP().getName());
		assertEquals("name3",sr1.getAllResults().get(1).getP().getName());
		assertEquals("name1",sr1.getAllResults().get(2).getP().getName());
		assertEquals("name4",sr1.getAllResults().get(3).getP().getName());
		assertEquals("name5",sr1.getAllResults().get(4).getP().getName());
		
		
	}

	@Test
	public void testDistanceScorer() {
		Ranker subOnlyRanker = new Ranker().addScorer(new DistanceScorer());
		Participant p = Participant.createParticipantForTest("abc",
				new Location(17.0, 75.12, "some-location", 40), "abc@ui.com");
		subOnlyRanker.rank(sr1, p);

		assertEquals("name2", sr1.getAllResults().get(0).getP().getName());
		assertEquals("name3", sr1.getAllResults().get(1).getP().getName());
		assertEquals("name1", sr1.getAllResults().get(2).getP().getName());

		assertEquals("name1", sr1.getHas().get(0).getP().getName());
		assertEquals("name2", sr1.getNeed().get(0).getP().getName());
		assertEquals("name3", sr1.getNeed().get(1).getP().getName());
	}

	@Test
	public void testSubjectScorer() {
		Ranker subOnlyRanker = new Ranker()
				.addScorer(new SubjectCorrelationScorer());
		Participant p = Participant.createParticipantForTest("abc",
				new Location(17.0, 75.0, "some-location", 40), "abc@ui.com");
		subOnlyRanker.rank(sr1, p);

		assertEquals("name1", sr1.getAllResults().get(0).getP().getName());
		assertEquals("name3", sr1.getAllResults().get(1).getP().getName());

		assertEquals("name1", sr1.getHas().get(0).getP().getName());
		assertEquals("name3", sr1.getNeed().get(0).getP().getName());
	}
}
