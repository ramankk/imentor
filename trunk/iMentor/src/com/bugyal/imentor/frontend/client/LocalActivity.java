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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
		
		flowpanel.setSize("750px", "250px");
		VerticalPanel vp = new VerticalPanel();
		vp.add(flowpanel);
		vp.setCellVerticalAlignment(flowpanel, HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setCellHorizontalAlignment(flowpanel, HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(vp);
		
		// TODO(sridhar): Show it as loading... change the mouse icon to loading..  
		showWaitCursor();
		getDataFeeds();
	}
	
	public void getDataFeeds() {
		service.localActivity(new AsyncCallback<SearchResponse>() {

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
			getDataFeeds();
		}
	}

}
