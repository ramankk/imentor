package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.shared.SearchResult;

public class SearchResultFactory {

	enum Type {
		ForHomeWidget,
		ForProfileWidget,
	};
	
	private Type type = Type.ForHomeWidget;
	
	public SearchResultFactory(Type type) {
		this.type = type;
	}
	
	private MapUI mapUI = null;
	
	public void setMapUI(MapUI mapUI) {
		this.mapUI = mapUI;
	}
	
	public SearchResultWidgetInterface create(boolean isEven) {
		if (type == Type.ForHomeWidget) {
			return new SearchResultWidget(isEven);
		}
		else if(type == Type.ForProfileWidget) {
			return new SearchResultWidgetForProfile(isEven, this.mapUI);
		}
		return null;
	}
	
	public static interface SearchResultWidgetInterface {
		public void setEvenRow(boolean even);
		public void setResult(SearchResult result);
		public void clear();
	}
}
