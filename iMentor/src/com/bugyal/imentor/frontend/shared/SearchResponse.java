package com.bugyal.imentor.frontend.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResponse implements IsSerializable {

	private List<SearchResult> has = new ArrayList<SearchResult>();
	private List<SearchResult> need = new ArrayList<SearchResult>();
	
	public SearchResponse() {
	}

	public List<SearchResult> getHas() {
		return has;
	}

	public void setHas(List<SearchResult> has) {
		this.has = has;
	}

	public List<SearchResult> getNeed() {
		return need;
	}

	public void setNeed(List<SearchResult> need) {
		this.need = need;
	}

	public List<SearchResult> getAllResults() {
		List<SearchResult> rList = new ArrayList<SearchResult>();
		rList.addAll(has);
		rList.addAll(need);
		Collections.sort(rList);
		return rList;
	}

	public void setAllResults(List<SearchResult> all) {
		clear();
		for (SearchResult searchResult : all) {
			add(searchResult);
		}
	}

	public void clear() {
		has.clear();
		need.clear();
	}

	public int size() {
		return need.size() + has.size();
	}

	public void add(SearchResult searchResult) {
		if (searchResult.isTypeParticipant()) {
			if (searchResult.isHas()) {
				has.add(searchResult);
			} else {
				need.add(searchResult);
			}
		} else {
			need.add(searchResult);
		}

	}

}
