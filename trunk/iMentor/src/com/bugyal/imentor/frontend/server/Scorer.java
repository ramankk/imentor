package com.bugyal.imentor.frontend.server;

import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.bugyal.imentor.server.data.Participant;

public interface Scorer {
	public void applyScores(List<SearchResult> rList, Participant p);
}
