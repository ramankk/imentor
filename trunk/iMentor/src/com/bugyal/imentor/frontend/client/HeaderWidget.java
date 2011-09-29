package com.bugyal.imentor.frontend.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class HeaderWidget extends Composite {

	private MainPageWidget mainPage = null;
	private UserDetails userDetails = null;
	private MenuBar menuBar;
	MentorServiceAsync service;
	private boolean newUser = false, initProfileWidget=false;
	
	private HeaderWidget me;
	
	public HeaderWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		menuBar = new MenuBar(false);
		horizontalPanel.add(menuBar);
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		me = this;
		if (mainPage == null) {
			// defer loading of mainPagewidget until session is created.
			mainPage = new MainPageWidget(me);
		}
		initWidget(horizontalPanel);
	}
	
	private AsyncCallback<Boolean> sessionCallback = new AsyncCallback<Boolean>() {

		@Override
		public void onFailure(Throwable caught) {
			initProfileWidget=true;
			setViewForNewUser(initProfileWidget);
		}

		@Override
		public void onSuccess(Boolean result) {
			
			if (result) {
				initProfileWidget = false;
				setViewToReturningUser();
			} else {
				initProfileWidget=true;
				setViewForNewUser(initProfileWidget);
			}
		}};
	
	public void init() {
		if (userDetails.getEmail() != null || userDetails.getEmail().equals("")) {
			service.createSession(userDetails.getEmail(), "facebook", 
					userDetails.getFbId(), sessionCallback);
		} else {
			initProfileWidget=true;
			setViewForNewUser(initProfileWidget);
		}
	}
	
	private void setViewToReturningUser() {
		newUser = false;
		mainPage.showHomeWidget();
		initMenuBar(menuBar);
	}

	private void setViewForNewUser(boolean initProfileWidget) {
		newUser = true;
		initMenuBar(menuBar);
		mainPage.showProfilePanel(initProfileWidget);
	}
	
	private void initMenuBar(MenuBar menuBar) {
		menuBar.clearItems();
		if (!newUser) {
			MenuItem mntmHome = new MenuItem("Home", false, homeCommand());
			menuBar.addItem(mntmHome);
			menuBar.addSeparator(new MenuItemSeparator());
		}

		MenuItem mntmProfile = new MenuItem("Profile", false, profileCommand());
		menuBar.addItem(mntmProfile);
		menuBar.addSeparator(new MenuItemSeparator());

		if (!newUser) {
			MenuItem mntmBuzz = new MenuItem("Opportunities", false,
					opportunityCommand());
			menuBar.addItem(mntmBuzz);
			menuBar.addSeparator(new MenuItemSeparator());
		}
		
		MenuItem searchData = new MenuItem("Search", false, searchCommand());
		menuBar.addItem(searchData);
		menuBar.addStyleName("menuBar");
	}
	
	public MainPageWidget getMainPage() {
 	    return mainPage;
	}

	public Command opportunityCommand() {
		return new Command() {
			@Override
			public void execute() {
				if (!newUser) {
					mainPage.showOpportunityPanel();
				}
			}
		};
	}

	public Command homeCommand() {
		return new Command() {
			@Override
			public void execute() {
				if (!newUser) {
					mainPage.showHomeWidget();
				}
			}
		};
	}

	public Command profileCommand() {
		return new Command() {
			@Override
			public void execute() {
				mainPage.showProfilePanel(initProfileWidget);
			}
		};
	}

	public Command searchCommand() {
		return new Command() {
			@Override
			public void execute() {
				mainPage.showSearchPanel();
			}
		};
	}
	
	public UserDetails getUserDetails() {
		return this.userDetails;
	}

	// for testing
	public void setEmailForTest(String email) {
		this.userDetails.setEmail(email);
		service.createSession(userDetails.getEmail(), "facebook",
				userDetails.getFbId(), sessionCallback);
	}

	public void setNewUser(boolean b) {
		if (newUser != b) {
			init();
		}
	}
}