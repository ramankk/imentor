package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class IMentor implements EntryPoint {

	TextBox tb, tb1;
	ToMeWidget tw;
	LocalActivity la;
	MentorServiceAsync service;

	public void onModuleLoad() {
		RootPanel.get("head").add(new HeaderWidget());
		tb = new TextBox();
		Button button = new Button("set user");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(tb);
		hp.add(button);

		tb1 = new TextBox();
		Button btn= new Button("Generate data");
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(tb1);
		hp1.add(btn);
		
		
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (tw == null) {
					tw = new ToMeWidget(tb.getText());
					la = new LocalActivity(tb.getText());
					RootPanel.get("tome").add(tw);
					RootPanel.get("activity").add(la);
				} else {
					tw.getDataFeeds(tb.getText());
					la.getDataFeeds(tb.getText());
				}
				
			}
		});
		
		btn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int range = Integer.parseInt(tb1.getText());
				service = (MentorServiceAsync) GWT.create(MentorService.class);
				service.generateRandomData(range, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Exception : " + caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Data generation complete ");
					}
				});					
			}
			
		});
		
		Button b = new Button("Delete Datastore Records");
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(b);
		b.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				service = (MentorServiceAsync) GWT.create(MentorService.class);
				service.deleteRecords(new AsyncCallback<Long>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to delete records");						
					}

					@Override
					public void onSuccess(Long result) {
						Window.alert("No of records deleted : "+result);
					}
					
				});
			}
			
		});
		
		RootPanel.get().add(hp);
		RootPanel.get().add(hp1);
		RootPanel.get().add(hp2);
	}
}