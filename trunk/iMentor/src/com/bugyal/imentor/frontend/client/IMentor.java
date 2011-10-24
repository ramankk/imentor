package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

public class IMentor implements EntryPoint {

	public static final boolean TEST_MODE_FLAG = true;
	public static final String PATH = "Path";

	public void onModuleLoad() {
		showWaitCursor();
		checkFbLogin(this);
		Anchor feedback = new Anchor("Feedback");
		feedback.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Comment c = new Comment();
				c.show();
				c.center();
			}
		});
		RootPanel.get("feedback").add(feedback);
	}

	public void loadApp(String fbId, String email, String name) {
		UserDetails userDetails = new UserDetails();
		userDetails.setName(name);
		userDetails.setEmail(email);
		userDetails.setFbId(fbId);

		if (email == null || email.trim().equals("")) {
			Cookies.setCookie(PATH, "true");
			Window.alert("Error occured. Sorry we couldn't find your details");
			return; // don't load the application.
		}
		if (Cookies.getCookie(PATH) == null
				|| Cookies.getCookie(PATH) == "false") {
			DOM.getElementById("bar")
					.setAttribute("style", "visibility:hidden");

		} else {
//			Cookies.removeCookie(PATH);
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
		$wnd.showIMentorApp = function(email, name, id) {
			x.@com.bugyal.imentor.frontend.client.IMentor::loadApp(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(email, name, id);
		};
	}-*/;

	public native void setShowTrigger2(IMentor x)/*-{
		$wnd.showIMentorApp2 = function() {
			x.@com.bugyal.imentor.frontend.client.IMentor::logout()();
		};
	}-*/;

	public void logout() {
		RootPanel.get("middle").clear();
	}
	
	public static void showWaitCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}

	public static void showDefaultCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}
	
	public native void checkFbLogin(IMentor o)/*-{
	$wnd.FB
			.login(
					function(response) {
						if (response.session) {
							$wnd.FB
									.api(
											'/me',
											function(response) {
												o.@com.bugyal.imentor.frontend.client.IMentor::loadApp(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(response.id,response.email,response.name);

											});
						} else {
							alert("User cancelled login or did not fully authorize.");
						}
					}, {
						perms : 'email'
					});
}-*/;		
}