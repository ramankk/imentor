package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class MainPageWidget extends Composite {

	private ToMeWidget myFeeds = null;
	private LocalActivity localActivity = null;

	private FlexTable homePanel = new FlexTable();

	private SearchWidget searchWidget = new SearchWidget();
	private FlexTable searchPanel = new FlexTable();

	private FlexTable pulsePanel = new FlexTable();
	private PulseWidget pulseWidget = new PulseWidget();
	
	private ProfileWidget profilePanel = null;
	private OpportunityPanel opportunityPanel = null;

	private FlexTable ft = new FlexTable();

	private static Label statusMessage = new Label();
	static Timer msgTimer = new Timer() {

		public void run() {
			statusMessage.setText("");
		}

	};

	private HeaderWidget menuPanel = null;

	public MainPageWidget(HeaderWidget header) {
		this.menuPanel = header;

		this.myFeeds = new ToMeWidget(header);
		this.localActivity = new LocalActivity(header);

		homePanel.setWidget(1, 0, myFeeds);
		homePanel.setWidget(2, 0, localActivity);

		searchPanel.setWidget(0, 0, searchWidget);

		pulsePanel.setWidget(0, 0, pulseWidget);
		// profilePanel = new ProfileWidget(this);
		opportunityPanel = new OpportunityPanel();

		ft.setWidget(0, 0, statusMessage);
		ft.getCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(ft);
	}

	public UserDetails getUserDetails() {
		return this.menuPanel.getUserDetails();
	}

	public void showHomeWidget() {
		myFeeds.reload();
		localActivity.reload();
		ft.setWidget(1, 0, homePanel);
	}

	public void showSearchPanel() {
		ft.setWidget(1, 0, searchPanel);
	}

	public void showPulsePanel() {
		pulseWidget.runMapPulse();
		ft.setWidget(1, 0, pulsePanel);
	}
	
	public void showProfilePanel(boolean initProfileWidget) {
		if (initProfileWidget || profilePanel == null) {
			profilePanel = new ProfileWidget(this);
		}
		profilePanel.init();
		ft.setWidget(1, 0, profilePanel);
	}

	public void showOpportunityPanel() {
		opportunityPanel.getOpportunitiesById();
		ft.setWidget(1, 0, opportunityPanel);
	}

	public HeaderWidget getHeaderWidget() {
		return menuPanel;
	}

	public static void setMessage(String message) {
		statusMessage.setText(message);
		statusMessage.setStyleName("statusMsg");
		msgTimer.schedule(5000);

	}

	public static void setErrorMessage(String errorMessage) {
		statusMessage.setText(errorMessage);
		statusMessage.setStyleName("statusErrorMsg");
		msgTimer.schedule(5000);
	}

}
