package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpportunityDialogBox extends DialogBox implements ClickHandler {
	SubjectsSuggestWidget subWidget = new SubjectsSuggestWidget();
	MentorServiceAsync service;
	TextArea txtMessage;
	TextArea tbLocation = new TextArea();
	Button btnCreate, btnCancel, btnClear;
	LocationData lData = new LocationData();
	MapUI mapUI;

	public OpportunityDialogBox() {

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		setSize("400px", "608px");
		setHTML("Opportunity");

		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail to get Subjects list");
			}

			@Override
			public void onSuccess(List<String> result) {
				subWidget.addMoreSubjects(result);
			}
		};
		service.getSubjects(callback);

		setWidget(horizontalPanel);
		horizontalPanel.setSize("750px", "558px");

		VerticalPanel verticalPanel = new VerticalPanel();
		horizontalPanel.add(verticalPanel);
		verticalPanel.setSize("259px", "558px");

		Label lblSubjects = new Label("Subjects:");
		verticalPanel.add(lblSubjects);
		verticalPanel.add(subWidget);

		Label lblLocation = new Label("Location:");
		verticalPanel.add(lblLocation);

		verticalPanel.add(tbLocation);
		tbLocation.setText(lData.getLocation());
		tbLocation.setSize("245px", "40px");
		tbLocation.setText("Please, Use the Map");

		Label lblMessage = new Label("Message:");
		verticalPanel.add(lblMessage);

		txtMessage = new TextArea();
		verticalPanel.add(txtMessage);
		txtMessage.setSize("245px", "90px");

		btnClear = new Button("Clear");
		btnClear.addClickHandler(this);
		btnCreate = new Button("Create");
		btnCreate.addClickHandler(this);
		btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(this);

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(btnClear);
		hp.add(btnCreate);
		hp.add(btnCancel);
		verticalPanel.add(hp);

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setSize("258px", "30px");

		horizontalPanel.add(verticalPanel);
		mapUI = new MapUI(false, tbLocation);
		horizontalPanel.add(mapUI);
	}

	@Override
	public void onClick(ClickEvent event) {
		lData = mapUI.getLocationDetails();
		if (event.getSource() == btnCreate) {

			if (!(subWidget.selected.getSubjects().isEmpty())
					&& tbLocation.getText().contains("Please, Use the Map")) {

				OpportunityVO oppVO = new OpportunityVO(null,
						subWidget.selected.getSubjects(), 0, 0, lData
								.getLatitude(), lData.getLongitude(), 0,
						tbLocation.getText());
				service.createOpportunity(oppVO,
						new AsyncCallback<OpportunityVO>() {

							@Override
							public void onFailure(Throwable caught) {
								Window
										.alert("Sorry, Unble to Create the Opportunity"+caught.getMessage());
							}

							@Override
							public void onSuccess(OpportunityVO result) {
								Window
										.alert("You Have successfully created an Opportunity");
								hideOpportunityDialogBox();
							}

						});
			}

		}

		if (event.getSource() == btnCancel) {
			subWidget.selected.clearAll();
			tbLocation.setText("Please, Use the Map");
			txtMessage.setText("");
			hideOpportunityDialogBox();
		}

		if (event.getSource() == btnClear) {
			subWidget.selected.clearAll();
			tbLocation.setText("Please, Use the Map");
			txtMessage.setText("");
		}

	}

	protected void hideOpportunityDialogBox() {
		this.hide();
	}
}