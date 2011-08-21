package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class LocalActivity extends Composite {
	MentorServiceAsync service;
	SearchResponseWidget allResults;
		
	FlowPanel flowpanel;
	HeaderWidget header;
	String lastEmailId = null;
	
	public LocalActivity(HeaderWidget header) {
		this.header = header;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		allResults = new SearchResponseWidget();
		flowpanel = new FlowPanel();
		flowpanel.add(allResults);
		
		flowpanel.setSize("500px", "250px");
		
		initWidget(flowpanel);
		
		// TODO(sridhar): Show it as loading... change the mouse icon to loading..  
		showWaitCursor();
		getDataFeeds(header.getUserDetails().getEmail());
	}
	
	public void getDataFeeds(String emailId) {
		service.localActivity(emailId, new AsyncCallback<SearchResponse>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Failed to fetch for local activies" + caught.getMessage());
			}

			@Override
			public void onSuccess(SearchResponse result) {
				List<SearchResult> all = new ArrayList<SearchResult>();
				all.addAll(result.getNeed());
				all.addAll(result.getHas());
				
				showDefaultCursor();
				allResults.setResults(all);				
			}
			
		});
	}
	
	public static void showWaitCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}
	 
	public static void showDefaultCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}

	public void reloadIfNeeded() {
		if (! header.getUserDetails().getEmail().equals(lastEmailId)) {
			getDataFeeds(header.getUserDetails().getEmail());
		}
	}

}
