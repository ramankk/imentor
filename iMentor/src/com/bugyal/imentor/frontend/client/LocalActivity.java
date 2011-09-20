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
import com.google.gwt.user.client.ui.VerticalPanel;

public class LocalActivity extends Composite {
	MentorServiceAsync service;
	SearchResponseWidget allResults;

	FlowPanel flowpanel;
	TabPanel tabPanel;
	HeaderWidget header;
	String lastEmailId = null;

	public LocalActivity(HeaderWidget header) {
		this.header = header;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		allResults = new SearchResponseWidget();
		flowpanel = new FlowPanel();
		flowpanel.add(allResults);

		tabPanel = new TabPanel();
		tabPanel.add(flowpanel, "Local Stream");
		tabPanel.selectTab(0);

		tabPanel.setWidth("700px");
		tabPanel.addStyleName("table-center");

		VerticalPanel vp = new VerticalPanel();
		vp.add(tabPanel);

		initWidget(vp);
		
		// getDataFeeds();
	}

	public void getDataFeeds() {
		showWaitCursor();
		service.localActivity(new AsyncCallback<SearchResponse>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Failed to fetch for local activies"
						+ caught.getMessage());
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

	public void reload() {
		getDataFeeds();
	}

}
