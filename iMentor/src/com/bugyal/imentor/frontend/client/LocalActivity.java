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
		
	public LocalActivity(String emailId) {
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		allResults = new SearchResponseWidget();
		Label lActivity = new Label("Loacl Activity");
		flowpanel = new FlowPanel();
		flowpanel.add(allResults);
		
		flowpanel.setSize("500px", "250px");
		
		initWidget(flowpanel);
		
		// TODO(sridhar): Show it as loading... change the mouse icon to loading..  
		showWaitCursor();
		getDataFeeds(emailId);
	}
	
	public void getDataFeeds(String emailId) {
		service.localActivity(emailId, new AsyncCallback<SearchResponse>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Failed to fetch for LOCAL ACTIVITIES..!");
			}

			@Override
			public void onSuccess(SearchResponse result) {
				Window.alert("Total Records are " + (result.getNeed().size() + result.getHas().size()));		
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

}
