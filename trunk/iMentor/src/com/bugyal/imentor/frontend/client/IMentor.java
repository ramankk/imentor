
package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

import com.google.gwt.user.client.ui.RootPanel;

public class IMentor implements EntryPoint {

	public static final boolean TEST_MODE_FLAG = true;

	public void onModuleLoad() {
		setShowTrigger(this);
		setShowTrigger2(this);
		Anchor feedback = new Anchor("Feedback");
		feedback.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				Comment c = new Comment("test@kawanan.com");
				c.show();
				c.center();
				Window.alert("working..");
			}
			
		});
		RootPanel.get("feedback").add(feedback);
	}
	
	public void loadApp(String email, String name, String fbId){
		UserDetails userDetails = new UserDetails();
		userDetails.setName(name);
		userDetails.setEmail(email);
		userDetails.setFbId(fbId);
		
		if (email == null || email.trim().equals("")) {
			Window.alert("Error occured. Sorry we couldn't find your details");
			return; // don't load the application.
		}
		
		HeaderWidget headerWidget = new HeaderWidget(userDetails);
		headerWidget.init();
		RootPanel.get("head").add(headerWidget);
		RootPanel.get("middle").add(headerWidget.getMainPage());
		
		if (TEST_MODE_FLAG) {
			RootPanel.get("imentortest").add(new TestingWidget(headerWidget));
		}
	}
	
	public native void setShowTrigger(IMentor x)/*-{
    	$wnd.showIMentorApp = function (email, name, id) {
			x.@com.bugyal.imentor.frontend.client.IMentor::loadApp(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(email, name, id);
    	};
	}-*/;
	
	public native void setShowTrigger2(IMentor x)/*-{
		$wnd.showIMentorApp2 = function () {
			x.@com.bugyal.imentor.frontend.client.IMentor::logout()();
		};
	}-*/;
	
	public void logout(){
		RootPanel.get("middle").clear();
	}
}