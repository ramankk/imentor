
package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class IMentor implements EntryPoint {

	public static final boolean TEST_MODE_FLAG = true;

	public void onModuleLoad() {
		setShowTrigger(this);
		setShowTrigger2(this);
	}
	
	public void loadApp(String fbId){
		UserDetails userDetails = new UserDetails();
		userDetails.setName(RootPanel.get("name").getElement().getInnerHTML());
		userDetails.setEmail(RootPanel.get("email").getElement().getInnerHTML());
		userDetails.setFbId(fbId);
		
		if (userDetails.getEmail() == null || userDetails.getEmail().trim().equals("")) {
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
    	$wnd.showIMentorApp = function (s) {
			x.@com.bugyal.imentor.frontend.client.IMentor::loadApp(Ljava/lang/String;)(s);
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