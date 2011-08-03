package com.bugyal.imentor.frontend.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResponse implements IsSerializable {
	
	private List<SearchResult> has;
	private List<SearchResult> need;
	
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
	
}
