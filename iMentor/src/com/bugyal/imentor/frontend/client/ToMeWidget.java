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
	
	String lastEmailId = null;
	HeaderWidget header = null;
	
	// Constructor for ToMeWidget
	public ToMeWidget(HeaderWidget header) {
		this.header = header;
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

		tabPanel.setSize("480px", "250px");
		tabPanel.addStyleName("table-center");

		initWidget(tabPanel);

		showWaitCursor();
		getDataFeeds();
	}

	// Method to Get data from DataStore
	public void getDataFeeds() {

		service.feedToMe(new AsyncCallback<SearchResponse>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("unable to load" + caught.getMessage());
				showDefaultCursor();
			}

			@Override
			public void onSuccess(SearchResponse result) {
				List<SearchResult> all = new ArrayList<SearchResult>();

				all.addAll(result.getAllResults());
				showDefaultCursor();

				allResults.setResults(all);
				hasResults.setResults(result.getHas());
				needResults.setResults(result.getNeed());
				
				lastEmailId = header.getUserDetails().getEmail();
			}
		});
	}

	public static void showWaitCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}

	public static void showDefaultCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}

	public void reload() {
		getDataFeeds();
	}
}
