package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class HeaderWidget extends Composite {
	private MainPageWidget mainPage = null;
	private UserDetails userDetails = null;
	private MenuBar menuBar;
	
	public HeaderWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("377px");
		menuBar = new MenuBar(false);
		horizontalPanel.add(menuBar);
		initWidget(horizontalPanel);
	}
	
	public void init() {
		mainPage = new MainPageWidget(this);
		initMenuBar(menuBar);
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

		if (IMentor.TEST_MODE_FLAG) {
			MenuItem randomData = new MenuItem("Seed Data", false,
					seedRandomDataCommand());
			menuBar.addItem(randomData);
		}
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
				mainPage.showOpportunityPanel();
			}
		};
	}

	public Command homeCommand() {
		return new Command() {
			@Override
			public void execute() {
				mainPage.showHomeWidget();
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
	}
}