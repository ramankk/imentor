package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class HeaderWidget extends Composite {

	
	public HeaderWidget() {
		initWidget(getHeaderWidget());
	}

	public HorizontalPanel getHeaderWidget() {
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
		
		MenuItem mntmBuzz = new MenuItem("Add Opportunity", false, opportunityCommand());
		menuBar.addItem(mntmBuzz);
		
		return horizontalPanel;
	}

	public Command opportunityCommand() {
		return new Command(){

			@Override
			public void execute() {
				
				OpportunityDialogBox opportunityDialogBox = new OpportunityDialogBox();
				opportunityDialogBox.show();
				opportunityDialogBox.center();
			}
			
		};
	}

	public Command homeCommand() {
		return new Command(){

			@Override
			public void execute() {
				
				
			}
			
		};
	}
	
	public Command profileCommand() {
		return new Command(){

			@Override
			public void execute() {
				ProfileDialogBox profileDialogBox = new ProfileDialogBox();
				profileDialogBox.show();
				profileDialogBox.center();
				
			}
			
		};
	}

	
}