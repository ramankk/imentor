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
	private boolean newUser = false;
	
	public HeaderWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("377px");
		menuBar = new MenuBar(false);
		horizontalPanel.add(menuBar);
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		mainPage = new MainPageWidget(this);
		initWidget(horizontalPanel);
	}
	
	private AsyncCallback<Boolean> sessionCallback = new AsyncCallback<Boolean>() {

		@Override
		public void onFailure(Throwable caught) {
			setViewForNewUser();
		}

		@Override
		public void onSuccess(Boolean result) {
			if (result) {
				setViewToReturningUser();
			} else {
				setViewForNewUser();
			}
		}};
	
	public void init() {
		if (userDetails.getEmail() != null || userDetails.getEmail().equals("")) {
			service.createSession(userDetails.getEmail(), "facebook", 
					userDetails.getFbId(), sessionCallback);
		} else {
			setViewForNewUser();
		}
	}
	
	private void setViewToReturningUser() {
		newUser = false;
		mainPage.showHomeWidget();
		initMenuBar(menuBar);
	}

	private void setViewForNewUser() {
		newUser = true;
		initMenuBar(menuBar);
		mainPage.showProfilePanel();
	}
	
	private void initMenuBar(MenuBar menuBar) {
		MenuItem mntmHome = new MenuItem("Home", false, homeCommand());
		menuBar.addItem(mntmHome);

		MenuItemSeparator separator = new MenuItemSeparator();
		menuBar.addSeparator(separator);

		MenuItem mntmProfile = new MenuItem("Profile", false, profileCommand());
		menuBar.addItem(mntmProfile);

		MenuItemSeparator separator_1 = new MenuItemSeparator();
		menuBar.addSeparator(separator_1);

		MenuItem mntmBuzz = new MenuItem("Opportunity", false,
				opportunityCommand());
		menuBar.addItem(mntmBuzz);

		MenuItem searchData = new MenuItem("Search", false, searchCommand());
		menuBar.addItem(searchData);

//		if (IMentor.TEST_MODE_FLAG) {
//			MenuItem randomData = new MenuItem("Seed Data", false,
//					seedRandomDataCommand());
//			menuBar.addItem(randomData);
//		}
	}
	
	public MainPageWidget getMainPage() {
 	    return mainPage;
	}
	
	private Command seedRandomDataCommand() {
		return new Command() {

			@Override
			public void execute() {
				DataGenerator dataGenerator = new DataGenerator();
				dataGenerator.show();
				dataGenerator.center();
			}
		};
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
				mainPage.showProfilePanel();
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