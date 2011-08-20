package com.bugyal.imentor.frontend.server;

import java.util.List;

import com.beoui.geocell.GeocellUtils;
import com.beoui.geocell.model.Point;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.data.Participant;

public class DistanceScorer implements Scorer {

	@Override
	public void applyScores(List<SearchResult> rList, Participant p) {
		for (int i = 0; i < rList.size(); i++) {
			SearchResult result = rList.get(i);
			result.applyScore(getScore(p, result));
		}
	}

	private double getScore(Participant p, SearchResult result) {
		return 1000 / GeocellUtils.distance(p.getLocation(), new Point(result
				.getLatitude(), result.getLongitude()));
	}
}
