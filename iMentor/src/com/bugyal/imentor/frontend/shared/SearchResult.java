package com.bugyal.imentor.frontend.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResult implements IsSerializable, Comparable<SearchResult> {
	
	private boolean has;
	private ParticipantVO p;
	private OpportunityVO o;
	
	private List<String> subjects;
	
	// TODO(Raman, Sridhar): This is temporary, should Ideally use an Enum.
	private boolean isTypeParticipant;
	
	// DO NOT USE this constructor, its only there for GWT to serialize.. 
	public SearchResult() {
		
	}
	
	public SearchResult(ParticipantVO p, boolean has, List<String> subjects) {
		isTypeParticipant = true;
		this.p = p;
		this.has = has;
		// AppEngine arraylist = normal arraylist..
		this.subjects = appEngineListToSimpleList(subjects);
		// strip participantVO object to reduce network-foot-print.
		//this.p.trim();
	}
	
	public static final List<String> appEngineListToSimpleList(List<String> subjects) {
		List<String> returnList = new ArrayList<String>();
		for (String s : subjects) {
			returnList.add(s);
		}
		return returnList;
	}
	
	public SearchResult(OpportunityVO o, List<String> subjects) {
		// AppEngine arraylist = normal arraylist..
		this.o = o;
		this.isTypeParticipant = false;
		this.subjects = appEngineListToSimpleList(subjects);
		
		// strip opportunityVO object to reduce network-foot-print.
		//this.o.trim();
	}

	public boolean isHas() {
		return has;
	}

	public ParticipantVO getP() {
		return p;
	}

	public OpportunityVO getO() {
		return o;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public boolean isTypeParticipant() {
		return isTypeParticipant;
	}

	public double getLongitude() {
		if (isTypeParticipant) {
		    return p.getLongitude();
		} else {
			return o.getLongitude();
		}
	}
	
	public double getLatitude() {
		if (isTypeParticipant) {
		    return p.getLatitude();
		} else {
			return o.getLatitude();
		}
	}
	
	double score = 1d;
	
	public void applyScore(double score) {
		this.score *= score; 
	}
	
	public double getScore() {
		return this.score;
	}

	@Override
	public int compareTo(SearchResult o) {
		if (score < o.getScore()) {
			return 1;
		} else if (score > o.getScore()) {
			return -1;
		} else {
			return 0;
		}
	}
}
