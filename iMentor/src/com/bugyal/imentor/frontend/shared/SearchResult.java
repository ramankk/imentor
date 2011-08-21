package com.bugyal.imentor.frontend.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResult implements IsSerializable, Comparable<SearchResult> {
	
	private boolean has;
	private ParticipantVO p;
	private OpportunityVO o;

	private String distance;
	private List<String> subjects;
	
	// TODO(Raman, Sridhar): This is temporary, should Ideally use an Enum.
	private boolean isTypeParticipant;
	
	// DO NOT USE this constructor, its only there for GWT to serialize.. 
	public SearchResult() {
		
	}
	
	public SearchResult(ParticipantVO p, boolean has, List<String> subjects, double distance) {
		isTypeParticipant = true;
		this.p = p;
		this.has = has;
		this.distance = getDistanceString(distance);
		// AppEngine array list = normal array list..
		this.subjects = appEngineListToSimpleList(subjects);
	}
	
	public static final List<String> appEngineListToSimpleList(List<String> subjects) {
		List<String> returnList = new ArrayList<String>();
		for (String s : subjects) {
			returnList.add(s);
		}
		return returnList;
	}
	
	public SearchResult(OpportunityVO o, List<String> subjects, double distance) {
		this.o = o;
		this.isTypeParticipant = false;
		// AppEngine array list = normal array list..
		this.subjects = appEngineListToSimpleList(subjects);
		this.distance = getDistanceString(distance);
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
	
	public String getDistance() {
		return distance;
	}
	
	public String getDistanceString(double distanceInMtrs) {
		int dist = (int) distanceInMtrs;
		if (dist < 1000) {
			return dist + " meters";
		} else {
			return ((int)(dist/100))/10 + " kms"; 
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
