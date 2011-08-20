package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public class MainPageWidget extends Composite {
	private ToMeWidget myFeeds = new ToMeWidget("amicjxkhhg@kawanan.com");
	private LocalActivity localActivity = new LocalActivity("amicjxkhhg@kawanan.com");
	private FlexTable homePanel = new FlexTable();
	
	private SearchWidget searchWidget = new SearchWidget();
	private FlexTable searchPanel = new FlexTable();
	
	private ProfileWidget profilePanel = null;
	private OpportunityPanel opportunityPanel = null;
	
	private FlexTable ft = new FlexTable();
	
	private HeaderWidget menuPanel = null;
	
	public MainPageWidget(HeaderWidget header) {
		this.menuPanel = header;
		
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
