package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileWidget extends Composite implements ClickHandler {
	MentorServiceAsync service;
	SubjectsSuggestWidget subWidgetHas = new SubjectsSuggestWidget();
	SubjectsSuggestWidget subWidgetNeed = new SubjectsSuggestWidget();
	TextArea tbLocation = new TextArea();
	Button btnClear, btnSave;
	TextBox tbName, tbEmailId;
	LocationData lData = new LocationData();
	boolean status = false;
	long id;
	RadioButton rbMail, rbFemail;
	MapUI mapUI;

	MainPageWidget mainPage = null;
	
	public ProfileWidget(MainPageWidget mainPage) {
		this.mainPage = mainPage;
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		
		final UserDetails userDetails = mainPage.getUserDetails();
		service.getParticipantVOByEmailId(userDetails.getEmail(),
				new AsyncCallback<ParticipantVO>() {

					@Override
					public void onFailure(Throwable caught) {
						tbName.setText(userDetails.getName());
						tbEmailId.setText(userDetails.getEmail());
						status = false;
					}

					@Override
					public void onSuccess(ParticipantVO result) {
						if (result == null) {
							Window.alert("No participant found");
							return;
						}
						tbName.setText(result.getName());
						tbEmailId.setText(result.getEmail());
						tbLocation.setText(result.getLocationString());
						for (String sub : result.getHasSubjects())
							subWidgetHas.selected.add(sub);
						for (String sub : result.getNeedSubjects())
							subWidgetNeed.selected.add(sub);
						id = result.getId();

						status = true;
					}
				});

		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail to get Subjects list");
			}

			@Override
			public void onSuccess(List<String> result) {
				subWidgetHas.selected.clearAll();
				subWidgetNeed.selected.clearAll();
				subWidgetHas.addMoreSubjects(result);
				subWidgetNeed.addMoreSubjects(result);

			}
		};
		service.getSubjects(callback);

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

		HorizontalPanel hpName = new HorizontalPanel();

		tbName = new TextBox();
		hpName.add(new Label("Name:"));
		hpName.add(tbName);
		verticalPanel_1.add(hpName);

		HorizontalPanel hpGender = new HorizontalPanel();
		hpGender.add(new Label("Gender"));
		rbMail = new RadioButton("Gender", "M");
		rbFemail = new RadioButton("Gender", "F");
		rbMail.setChecked(true);
		hpGender.add(rbMail);
		hpGender.add(rbFemail);
		verticalPanel_1.add(hpGender);

		HorizontalPanel hpEmailId = new HorizontalPanel();

		tbEmailId = new TextBox();
		tbEmailId.setEnabled(false);
		hpEmailId.add(new Label("Mail Id"));
		hpEmailId.add(tbEmailId);
		verticalPanel_1.add(hpEmailId);

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
		horizontalPanel_2.setSpacing(30);
		horizontalPanel_2.setSize("258px", "30px");

		btnClear = new Button("Clear");
		horizontalPanel_2.add(btnClear);
		btnClear.addClickHandler(this);

		btnSave = new Button("Save");
		horizontalPanel_2.add(btnSave);
		btnSave.addClickHandler(this);

		mapUI = new MapUI(true, tbLocation);
		mapUI.setSize("400px", "400px");
		horizontalPanel.add(mapUI);
		
		initWidget(horizontalPanel);
	}

	@Override
	public void onClick(ClickEvent event) {

		if (event.getSource() == btnSave) {
			lData = mapUI.getLocationDetails();
			if (!(subWidgetHas.selected.getSubjects().isEmpty() && subWidgetNeed.selected
					.getSubjects().isEmpty()) && (tbLocation.getText() != null)) {

				ParticipantVO partVO = new ParticipantVO(id, tbName.getText(),
						"M", tbEmailId.getText(), lData.getLatitude(),
						lData.getLongitude(), tbLocation.getText(),
						lData.getRadius(), subWidgetHas.selected.getSubjects(),
						subWidgetNeed.selected.getSubjects());
				if (!status) {
					service.create(partVO, new AsyncCallback<ParticipantVO>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Unable to Create the Profile"
									+ caught.getMessage());
						}

						@Override
						public void onSuccess(ParticipantVO result) {
							mainPage.showHomeWidget();
						}
					});
				} else {
					service.update(partVO, new AsyncCallback<ParticipantVO>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Sorry, No changes has been made"
									+ caught.getMessage());
						}

						@Override
						public void onSuccess(ParticipantVO result) {
							Window.alert("Updated sucessfully");
							mainPage.showHomeWidget();
						}

					});
				}
			}
		}
		if (event.getSource() == btnClear) {
			tbName.setText(null);
			tbEmailId.setText(null);
			subWidgetHas.selected.clearAll();
			subWidgetNeed.selected.clearAll();
		}
	}
}