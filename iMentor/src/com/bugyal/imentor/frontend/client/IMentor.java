package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.reveregroup.gwt.facebook4gwt.Facebook;
import com.reveregroup.gwt.facebook4gwt.LoginButton;
import com.reveregroup.gwt.facebook4gwt.events.FacebookLoginEvent;
import com.reveregroup.gwt.facebook4gwt.events.FacebookLoginHandler;
import com.reveregroup.gwt.facebook4gwt.user.UserField;


public class IMentor implements EntryPoint {
	
	public LoginButton loginButton;
	public Label statusLabel,userName;
	
	public void onModuleLoad() {
		statusLabel = new Label();
		userName = new Label();
		Facebook.init("139577986094709");
		loginButton = new LoginButton();
		RootPanel.get().add(loginButton);
		Window.alert("OK");
		
		Facebook.login(new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("failure");
				
			}

			@Override
			public void onSuccess(Object result) {
				RootPanel.get("header").add(new HeaderWidget());
				
			}
			
		});
	/*	Facebook.addLoginHandler(new FacebookLoginHandler(){

			@Override
			public void loginStatusChanged(FacebookLoginEvent event) {
				if (event.isLoggedIn()){
					Window.alert("logged in");
							statusLabel.setText("Logged in");
						
				} else {
					Window.alert("OK");
					statusLabel.setText("No user logged in");
				}	
				
			}
			
		});
		
		Facebook.APIClient().users_getLoggedInUser(new AsyncCallback() {
		
			@Override
			public void onFailure(Throwable caught) {
			    //userInfoPanel.setVisible(false);
				RootPanel.get("header").add(new HeaderWidget());
			  }
				@Override
				public void onSuccess(Object result) {
					userName.setText(result.toString());
					
					//userInfoPanel.setVisible(true);
					
				}
			}, UserField.NAME);*/
		RootPanel.get().add(userName);
		RootPanel.get().add(statusLabel);
	}
}
