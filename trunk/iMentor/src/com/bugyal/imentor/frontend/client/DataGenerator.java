package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataGenerator extends DialogBox {
	TextBox txtBox;
	Button btn;
	VerticalPanel vp;
	MentorServiceAsync service;
	
	public DataGenerator() {
		setHTML("Data Generator");
		
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		txtBox = new TextBox();
		btn = new Button("Generate");
		vp = new VerticalPanel();
		Label l = new Label("Enter the range : ");
		HorizontalPanel hp = new HorizontalPanel();
		
		txtBox.setSize("100px", "20px");
		btn.setSize("100px", "20px");
		
		hp.add(l);
		hp.add(txtBox);
		vp.add(hp);
		vp.add(btn);
		vp.setSize("100px", "150px");
		setWidget(vp);
		btn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
					int range = Integer.parseInt(txtBox.getText());
					
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
	}

}
