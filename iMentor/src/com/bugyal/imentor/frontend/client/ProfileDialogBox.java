package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileDialogBox extends DialogBox implements ClickHandler {

	MentorServiceAsync service;
	SubjectsSuggestWidget subWidgetHas = new SubjectsSuggestWidget();
	SubjectsSuggestWidget subWidgetNeed = new SubjectsSuggestWidget();
	TextArea textArea_1, textArea;
	TextArea tbLocation = new TextArea();
	Button btnSave, btnCancel;
	Label lblName, lblGender, lblEmailId;
	LocationData lData = new LocationData();

	MapUI mapUI;

	public ProfileDialogBox() {

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		setSize("400px", "608px");
		setHTML("Profile");

		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail to get Subjects list");
			}

			@Override
			public void onSuccess(List<String> result) {
				subWidgetHas.addMoreSubjects(result);
				subWidgetNeed.addMoreSubjects(result);
			}

		};
		service.getSubjects(callback);

		setWidget(horizontalPanel);
		horizontalPanel.setSize("750px", "558px");

		VerticalPanel verticalPanel = new VerticalPanel();
		horizontalPanel.add(verticalPanel);
		verticalPanel.setSize("259px", "558px");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);
		horizontalPanel_1.setSize("255px", "129px");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		horizontalPanel_1.add(verticalPanel_1);
		verticalPanel_1.setSize("144px", "126px");

		lblName = new Label("Name");
		verticalPanel_1.add(lblName);

		lblGender = new Label("Gender");
		verticalPanel_1.add(lblGender);

		lblEmailId = new Label("Email ID");
		verticalPanel_1.add(lblEmailId);
		lblEmailId.setText("fakeemail" + Random.nextInt() + " @kawanan.com");

		Label lblLocation = new Label("Location:");
		verticalPanel.add(lblLocation);

		verticalPanel.add(tbLocation);
		tbLocation.setText(lData.getLocation());
		tbLocation.setSize("245px", "40px");

		Label lblSubjectsYouNeed = new Label("Subjects You know:");
		verticalPanel.add(lblSubjectsYouNeed);
		verticalPanel.add(subWidgetHas);

		Label lblSujectsYouWant = new Label("Sujects You Want:");
		verticalPanel.add(lblSujectsYouWant);
		verticalPanel.add(subWidgetNeed);

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_2);
		horizontalPanel_2.setSize("258px", "30px");

		btnSave = new Button("Save");
		horizontalPanel_2.add(btnSave);
		horizontalPanel_2.setCellHorizontalAlignment(btnSave,
				HasHorizontalAlignment.ALIGN_RIGHT);
		btnSave.addClickHandler(this);

		btnCancel = new Button("Cancel");
		horizontalPanel_2.add(btnCancel);
		btnCancel.addClickHandler(this);

		mapUI = new MapUI(true, tbLocation);

		horizontalPanel.add(mapUI);

	}

	@Override
	public void onClick(ClickEvent event) {

		if (event.getSource() == btnSave) {
			lData = mapUI.getLocationDetails();
			if (!(subWidgetHas.selected.getSubjects().isEmpty() && subWidgetNeed.selected
					.getSubjects().isEmpty())
					&& (tbLocation.getText() != null)) {
				Window.alert("save button called  "
						+ subWidgetHas.selected.getSubjects().size() + " :: "
						+ subWidgetNeed.selected.getSubjects().size());
				ParticipantVO partVO = new ParticipantVO(null, lblName
						.getText(), lblEmailId.getText(), lData.getLatitude(),
						lData.getLongitude(), tbLocation.getText(), lData
								.getRadius(), subWidgetHas.selected
								.getSubjects(), subWidgetNeed.selected
								.getSubjects());
				service.create(partVO, new AsyncCallback<ParticipantVO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Unable to save your data"
								+ caught.getMessage());

					}

					@Override
					public void onSuccess(ParticipantVO result) {

						Window.alert("Your data has been saved");
						hideProfileDialog();
					}
				});
			}

		}
		if (event.getSource() == btnCancel) {
			this.hide();
		}

	}

	protected void hideProfileDialog() {
		this.hide();

	}

}