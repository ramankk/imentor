package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IMentor implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final MentorServiceAsync rpcService = GWT
			.create(MentorService.class);

	public native JavaScriptObject getSession () /*-{
		return $wnd.FB.getSession();
	}-*/;
	
	public void registerMe() {
		
		
		
	}
	
	public void showOpportunity(OpportunityVO o) {
		
	}
	
	public Panel showNewParticipationForm() {
		HorizontalPanel hp = new HorizontalPanel();
		
		return hp;
	}
	
	public void showOppurtunitiesUI() {
		
	}
	
	public void showTools() {
		final HorizontalPanel hp = new HorizontalPanel();
		
		final Button find = new Button("Lookup");
		final ListBox subjects = new ListBox();
		subjects.addItem("Math");
		subjects.addItem("CS");
		
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				subjects.getItemText(subjects.getSelectedIndex());
			}};
		find.addClickHandler(handler );
		
		hp.add(subjects);
		hp.add(find);
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get().add(new AddParticipantWidget("nothing" + System.currentTimeMillis(), rpcService)); //getSession().toString()));
	}
}
