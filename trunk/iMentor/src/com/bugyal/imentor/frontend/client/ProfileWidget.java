package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileWidget extends Composite implements ClickHandler {
	MentorServiceAsync service;
	SubjectsSuggestWidget subWidgetHas = new SubjectsSuggestWidget(
			new ArrayList<String>());
	SubjectsSuggestWidget subWidgetNeed = new SubjectsSuggestWidget(
			new ArrayList<String>());
	TextArea tbLocation = new TextArea();
	Button btnClear, btnSave;
	TextBox tbName, tbEmailId;
	LocationData lData = new LocationData();
	boolean status = false;
	long id;
	RadioButton rbMale, rbFemale;
	MapUI mapUI;

	MainPageWidget mainPage = null;

	public ProfileWidget(MainPageWidget mainPage) {
		this.mainPage = mainPage;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		final UserDetails userDetails = mainPage.getUserDetails();
		service.getParticipantVOByEmailId(new AsyncCallback<ParticipantVO>() {

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
							subWidgetHas.add(sub);
						for (String sub : result.getNeedSubjects())
							subWidgetNeed.add(sub);
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
				subWidgetHas.clearAll();
				subWidgetNeed.clearAll();
				subWidgetHas.addMoreSubjects(result);
				subWidgetNeed.addMoreSubjects(result);
			}
		};
		service.getSubjects(callback);
		
		VerticalPanel mainPanel = new VerticalPanel();
				
		HorizontalPanel nameHorizontal = new HorizontalPanel();
		nameHorizontal.add(new Label("Name "));
		tbName = new TextBox();
		nameHorizontal.add(tbName);		
		HorizontalPanel genderHorizontal = new HorizontalPanel();
		rbMale = new RadioButton("Gender", "M");
		rbFemale = new RadioButton("Gender", "F");
		rbMale.setValue(true);
		genderHorizontal.add(new Label("Gender "));
		genderHorizontal.add(rbMale);
		genderHorizontal.add(rbFemale);
		HorizontalPanel mailHorizontal = new HorizontalPanel();
		tbEmailId = new TextBox();
		//TODO:(ravi) remove when you submit
		tbEmailId.setText("teja.cse596@gmail.com");
		tbEmailId.setEnabled(false);
		mailHorizontal.add(new Label("Mail Id"));
		mailHorizontal.add(tbEmailId);		
		
		HorizontalPanel personalHorizontal = new HorizontalPanel();
		personalHorizontal.setWidth("750px");
		personalHorizontal.add(nameHorizontal);
		personalHorizontal.add(genderHorizontal);
		personalHorizontal.add(mailHorizontal);
		personalHorizontal.setCellHorizontalAlignment(mailHorizontal, HasHorizontalAlignment.ALIGN_RIGHT);
		
		VerticalPanel locationVertical = new VerticalPanel();
		Label l= new Label("Location:");
		l.setHeight("29px");
		locationVertical.add(l);
		locationVertical.add(tbLocation);

		tbLocation.setText(lData.getLocation());
		tbLocation.setSize("215px", "45px");
		
		VerticalPanel knowSubVertical = new VerticalPanel();
		knowSubVertical.add(new Label("Subjects you know"));
		knowSubVertical.add(subWidgetHas);
		
		VerticalPanel wantSubVertical = new VerticalPanel();
		wantSubVertical.add(new Label("Subjects you want"));
		wantSubVertical.add(subWidgetNeed);
		
		HorizontalPanel middleHorizontal = new HorizontalPanel();
		middleHorizontal.setWidth("750px");
		middleHorizontal.add(locationVertical);
		middleHorizontal.add(knowSubVertical);
		middleHorizontal.add(wantSubVertical);
		middleHorizontal.setCellHorizontalAlignment(knowSubVertical, HasHorizontalAlignment.ALIGN_RIGHT);
		middleHorizontal.setCellHorizontalAlignment(wantSubVertical, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel buttonsHorizontal = new HorizontalPanel();

		btnClear = new Button("Clear");
		btnClear.addClickHandler(this);
		buttonsHorizontal.add(btnClear);
		
		btnSave = new Button("Save");
		btnSave.addClickHandler(this);
		buttonsHorizontal.add(btnSave);
		
		VerticalPanel topVertical = new VerticalPanel();
		topVertical.add(personalHorizontal);
		topVertical.add(middleHorizontal);
		topVertical.add(buttonsHorizontal);
		topVertical.setCellHorizontalAlignment(buttonsHorizontal, HasHorizontalAlignment.ALIGN_RIGHT);
		topVertical.setHeight("180px");

		mainPanel.add(new HTML("<h3>Your Profile :</h3>"));
		mainPanel.add(topVertical);
		mapUI = new MapUI(true, tbLocation);	
		TabPanel map = new TabPanel();
		map.add(mapUI,"Map View");
		map.selectTab(0);
		mainPanel.add(map);
		
		initWidget(mainPanel);
	}

	@Override
	public void onClick(ClickEvent event) {

		if (event.getSource() == btnSave) {
			lData = mapUI.getLocationDetails();
			if (!(subWidgetHas.getSubjects().isEmpty() && subWidgetNeed
					.getSubjects().isEmpty())
					&& (tbLocation.getText() != null)) {

				ParticipantVO partVO = new ParticipantVO(id, tbName.getText(),
						"M", tbEmailId.getText(), lData.getLatitude(), lData
								.getLongitude(), tbLocation.getText(), lData
								.getRadius(), subWidgetHas.getSubjects(),
						subWidgetNeed.getSubjects());
				if (!status) {
					service.create(partVO, new AsyncCallback<ParticipantVO>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Unable to Create the Profile"
									+ caught.getMessage());
						}

						@Override
						public void onSuccess(ParticipantVO result) {
							mainPage.getHeaderWidget().setNewUser(false);
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
			subWidgetHas.clearAll();
			subWidgetNeed.clearAll();
		}
	}
}