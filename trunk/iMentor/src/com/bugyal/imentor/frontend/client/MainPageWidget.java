package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public class MainPageWidget extends Composite {
	private ToMeWidget myFeeds = new ToMeWidget("amicjxkhhg@kawanan.com");
	private LocalActivity localActivity = new LocalActivity("amicjxkhhg@kawanan.com");
	private FlexTable homePanel = new FlexTable();
	private FlexTable searchPanel = new FlexTable();
	private FlexTable ft = new FlexTable();
	private SearchWidget searchWidget = new SearchWidget();
	
	public MainPageWidget() {
		homePanel.setWidget(0, 0, myFeeds);
		homePanel.setWidget(1, 0, localActivity);
		
		searchPanel.setWidget(0, 0, searchWidget);
		
		showHomeWidget();
		initWidget(ft);
	}

	public void showHomeWidget() {
		ft.setWidget(1, 0, homePanel);
	}
	
	public void showSearchPanel() {
		ft.setWidget(1, 0, searchPanel);
	}

}
