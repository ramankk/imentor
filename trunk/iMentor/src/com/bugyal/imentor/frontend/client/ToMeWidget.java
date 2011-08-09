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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

public class ToMeWidget extends Composite {

	MentorServiceAsync service;
	SearchResponseWidget allResults;
	SearchResponseWidget hasResults;
	SearchResponseWidget needResults;
	
	TabPanel tabPanel;

	// Constructor for ToMeWidget
	public ToMeWidget(String EmailId) {
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		allResults = new SearchResponseWidget();
		hasResults = new SearchResponseWidget();
		needResults = new SearchResponseWidget();
		
		FlowPanel flowpanel;
		
		tabPanel = new TabPanel();

		flowpanel = new FlowPanel();
		flowpanel.add(allResults);
		tabPanel.add(flowpanel, "All");

		flowpanel = new FlowPanel();
		flowpanel.add(needResults); 
		tabPanel.add(flowpanel, "Need");

		flowpanel = new FlowPanel();
		flowpanel.add(hasResults); 
		tabPanel.add(flowpanel, "Has");

		tabPanel.selectTab(0);

		tabPanel.setSize("500px", "250px");
		tabPanel.addStyleName("table-center");
		
		initWidget(tabPanel);
		
		// TODO(sridhar): Show it as loading... change the mouse icon to loading..  
		showWaitCursor();
		getDataFeeds(EmailId);
	}

	// Method to Get data from DataStore
	public void getDataFeeds(String EmailId) {
		service.feedToMe(EmailId, new AsyncCallback<SearchResponse>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("unable to load");
			}

			@Override
			public void onSuccess(SearchResponse result) {
				List<SearchResult> all = new ArrayList<SearchResult>();
				all.addAll(result.getNeed());
				all.addAll(result.getHas());
				
				showDefaultCursor();
				
				allResults.setResults(all);
				hasResults.setResults(result.getHas());
				needResults.setResults(result.getNeed());
				// change back the mouse icon to normal pointer.
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
