package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;




public class IMentor implements EntryPoint {

   
	public void onModuleLoad() {
		RootPanel.get().add(new HeaderWidget());
		
	}	

	 
}