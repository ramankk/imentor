package com.bugyal.imentor.frontend.server;

import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.data.Participant;

public class SubjectCorrelationScorer implements Scorer {

	@Override
	public void applyScores(List<SearchResult> rList, Participant p) {
		for (int i = 0; i < rList.size(); i ++) {
			SearchResult result = rList.get(i);
			double score = result.getSubjects().size() + 0.0;
			result.applyScore(score);
		}
	}
}
