package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class HeaderWidget extends Composite {
	private OpportunityDialogBox opportunityDialogBox = new OpportunityDialogBox();
	private ProfileDialogBox profileDialogBox = new ProfileDialogBox("g7iz219iu9@kawanan.com", "test1");
	
	private MainPageWidget mainPage = null;
	
	public HeaderWidget(MainPageWidget mainPage, UserDetails userDetails) {
		this.mainPage = mainPage;
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("377px");
		Image image = new Image("");
		image.setPixelSize(100, 30);
		horizontalPanel.add(image);
		MenuBar menuBar = new MenuBar(false);
		horizontalPanel.add(menuBar);

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
		
		initWidget(menuBar);
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
				opportunityDialogBox.show();
				opportunityDialogBox.center();
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
				profileDialogBox.show();
				profileDialogBox.center();
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
}