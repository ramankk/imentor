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
	
	public SearchResultWidgetInterface create(boolean isEven) {
		if (type == Type.ForHomeWidget) {
			return new SearchResultWidget(isEven);
		}
		else if(type == Type.ForProfileWidget) {
			return new SearchResultWidgetForProfile(isEven);
		}
		return null;
	}
	
	public static interface SearchResultWidgetInterface {
		public void setEvenRow(boolean even);
		public void setResult(SearchResult result);
		public void clear();
	}
}
