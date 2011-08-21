package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public class MainPageWidget extends Composite {
	private ToMeWidget myFeeds = null;
	private LocalActivity localActivity = null;
	
	private FlexTable homePanel = new FlexTable();
	
	private SearchWidget searchWidget = new SearchWidget();
	private FlexTable searchPanel = new FlexTable();
	
	private ProfileWidget profilePanel = null;
	private OpportunityPanel opportunityPanel = null;
	
	private FlexTable ft = new FlexTable();
	
	private HeaderWidget menuPanel = null;
	
	public MainPageWidget(HeaderWidget header) {
		this.menuPanel = header;
		
		this.myFeeds = new ToMeWidget(header);
		this.localActivity = new LocalActivity(header);
		
		homePanel.setWidget(0, 0, myFeeds);
		homePanel.setWidget(1, 0, localActivity);
		
		searchPanel.setWidget(0, 0, searchWidget);
		
		profilePanel = new ProfileWidget(this);
		opportunityPanel = new OpportunityPanel(this);
		
		showHomeWidget();
		initWidget(ft);
	}

	public UserDetails getUserDetails() {
		return this.menuPanel.getUserDetails();
	}
	
	public void showHomeWidget() {
		myFeeds.reloadIfNeeded();
		localActivity.reloadIfNeeded();
		ft.setWidget(1, 0, homePanel);
	}
	
	public void showSearchPanel() {
		ft.setWidget(1, 0, searchPanel);
	}

	public void showProfilePanel() {
		ft.setWidget(1,	0, profilePanel);
	}

	public void showOpportunityPanel() {
		ft.setWidget(1, 0, opportunityPanel);
	}
}
