
package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class IMentor implements EntryPoint {

	public static final boolean TEST_MODE_FLAG = true;

	public void onModuleLoad() {
		setShowTrigger(this);
		setShowTrigger2(this);
	}
	
	public void loadApp(){
		UserDetails userDetails = new UserDetails();
		userDetails.setName(RootPanel.get("name").getElement().getInnerHTML());
		userDetails.setEmail(RootPanel.get("email").getElement().getInnerHTML());
		
		HeaderWidget headerWidget = new HeaderWidget(userDetails);
		headerWidget.init();
		RootPanel.get("head").add(headerWidget);
		RootPanel.get("middle").add(headerWidget.getMainPage());
		
		if (TEST_MODE_FLAG) {
			RootPanel.get("imentortest").add(new TestingWidget(headerWidget));
		}
	}
	
	public native void setShowTrigger(IMentor x)/*-{
    	$wnd.showIMentorApp = function () {
			x.@com.bugyal.imentor.frontend.client.IMentor::loadApp()();
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