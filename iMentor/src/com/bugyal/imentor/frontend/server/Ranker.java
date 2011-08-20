package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.data.Participant;

public class Ranker {
	public Ranker() {
	}
	
	List<Scorer> scorers = new ArrayList<Scorer>();
	
	public Ranker addScorer(Scorer scorer) {
		scorers.add(scorer);
		return this;
	}

	public SearchResponse rank(SearchResponse response, Participant p) {
		response.setAllResults(applyScorers(p, response.getAllResults()));
		return response;
	}

	private List<SearchResult> applyScorers(Participant p,
			List<SearchResult> rList) {
		for (Scorer s: scorers) {
			s.applyScores(rList, p);
		}
		
		Collections.sort(rList);
		return rList;
	}
}
