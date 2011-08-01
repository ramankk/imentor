package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.reveregroup.gwt.facebook4gwt.LoginButton;


public class IMentor implements EntryPoint {

   //  public static String API_KEY = "139577986094709";
	public void onModuleLoad() {
		
	//	exportMethods(this);
		initFacebookAPI();

//		Button b = new Button("Click me");
//		b.addClickHandler(new ClickHandler(){
//
//			@Override
//			public void onClick(ClickEvent event) {
//				getMe();
//			}
//			
//		});
//		RootPanel.get().add(b);
		RootPanel.get().add(new HTML("<fb:login-button></fb:login-button>"));
	}	

	private native String initFacebookAPI()
	/*-{
		$wnd.FB.init({appId: '139577986094709', status: true, cookie: true, xfbml: true});
		$wnd.FB.Event.subscribe('auth.sessionChange', function(response) {
		  if (response.session) {
		    // A user has logged in, and a new cookie has been saved
		    $wnd.alert('b4 login');
		    $wnd.onLogin();
		    
		  } else {
		    // The user has logged out, and the cookie has been cleared
		    $wnd.onLogout();
		  }		  			  	
		});
	}-*/;  

	private native void callAPI(String path, AsyncCallback<JavaScriptObject> callback) /*-{
		$wnd.FB.api(path, function(response) {
			// on error, this callback is never called in Firefox - why?
			if (!response) {
			    $wnd.alert('Error occured');
			} else if (response.error) {
				 $wnd.alert('1 Error occured');
				$wnd.alert($wnd.dump(response));
			    // call callback with the actual error
				$wnd.onAPICall(callback, null, response.error);
			} else if (response.data) {
				 $wnd.alert('2 Error occured');
				$wnd.alert($wnd.dump(response));
				// call callback with the actual json-array
				$wnd.onAPICall(callback, response.data, null);
			} else {
				 $wnd.alert('3 Error occured');
				$wnd.alert($wnd.dump(response));
				// call callback with the actual json-object
				$wnd.onAPICall(callback, response, null);
			} 
		});
	}-*/;


  
}