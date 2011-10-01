package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchWidget extends Composite implements ClickHandler,
		ChangeHandler {
	MapUI mapUI;
	Button searchBtn, clearBtn;
	MentorServiceAsync service;
	LocationData locationData;
	TextArea location;
	SubjectsSuggestWidget subjectsSuggestWidget;
	ListBox listBox;
	SearchResponse sResponse;
	TabPanel tabPanel_1 = new TabPanel();
	TabPanel switchView = new TabPanel();
	ParticipantVO participant;
	public static final String LOCATION_HELP = "Please Use the Map";

	SearchResponseWidget searchResponseWidget = new SearchResponseWidget();

	FlexTable resultsPanel = new FlexTable();

	public SearchWidget() {
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		location = new TextArea();
		location.setEnabled(false);
		location.setSize("213px", "43px");
		location.setText(LOCATION_HELP);
		location.setTitle(LOCATION_HELP);
		mapUI = new MapUI(false, location);

		subjectsSuggestWidget = new SubjectsSuggestWidget(
				new ArrayList<String>());
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Window.alert("Fail to get Subjects list");
			}

			@Override
			public void onSuccess(List<String> result) {
				subjectsSuggestWidget.addMoreSubjects(result);
			}

		};
		service.getSubjects(callback);

		AsyncCallback<ParticipantVO> getParticipantCallback = new AsyncCallback<ParticipantVO>() {

			@Override
			public void onSuccess(ParticipantVO result) {
				participant = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};
		service.getParticipantVOByEmailId(getParticipantCallback);

		VerticalPanel subjectsVertical = new VerticalPanel();
		subjectsVertical.setHeight("80px");
		subjectsVertical.add(new Label("Select Subjects"));
		subjectsVertical.add(subjectsSuggestWidget);

		VerticalPanel locationVertical = new VerticalPanel();
		locationVertical.setHeight("80px");
		Label l = new Label("Select Location:");
		l.setHeight("20px");
		locationVertical.add(l);
		locationVertical.add(location);

		VerticalPanel searchVertical = new VerticalPanel();

		listBox = new ListBox();
		listBox.setTitle("Filters the results based on the option Selected");
		listBox.addItem("All");
		listBox.addItem("Mentors");
		listBox.addItem("Mentees");
		HorizontalPanel dropdownHorizontal = new HorizontalPanel();
		dropdownHorizontal.add(new Label("Search By:"));
		dropdownHorizontal.add(listBox);

		HorizontalPanel btnHP = new HorizontalPanel();
		searchBtn = new Button("Search");
		searchBtn.addClickHandler(this);
		clearBtn = new Button("Clear");
		clearBtn.addClickHandler(this);

		btnHP.add(clearBtn);
		btnHP.add(searchBtn);

		searchVertical.add(dropdownHorizontal);
		searchVertical.add(btnHP);
		searchVertical.setCellHorizontalAlignment(btnHP,
				HasHorizontalAlignment.ALIGN_RIGHT);
		searchVertical.setHeight("80px");

		HorizontalPanel topHorizontal = new HorizontalPanel();
		topHorizontal.add(subjectsVertical);
		topHorizontal.add(locationVertical);
		topHorizontal.add(searchVertical);
		topHorizontal.setWidth("700px");
		tabPanel_1.add(topHorizontal, "Search");
		tabPanel_1.selectTab(0);
		tabPanel_1.setSize("700px", "150px");
		switchView.add(mapUI, "Map View");
		switchView.add(searchResponseWidget, "List View");
		switchView.setWidth("710px");
		switchView.selectTab(0);

		VerticalPanel mainPanel = new VerticalPanel();

		mainPanel.add(tabPanel_1);
		mainPanel.add(new HTML("<br>"));
		mainPanel.add(switchView);

		locationData = mapUI.getLocationDetails();

		initWidget(mainPanel);
	}

	@Override
	public void onClick(ClickEvent event) {

		List<String> hasSubs, needSubs;

		hasSubs = new ArrayList<String>();
		needSubs = new ArrayList<String>();

		if (event.getSource() == searchBtn) {
			showWaitCursor();
			locationData = mapUI.getLocationDetails();

			if (subjectsSuggestWidget.getSelected() == null) {
				// hasSubs = participant.getHasSubjects();
				// needSubs = participant.getNeedSubjects();
				MainPageWidget.setErrorMessage("No subjects Selected");

			} else {
				hasSubs = subjectsSuggestWidget.getSelected();
				needSubs = subjectsSuggestWidget.getSelected();
			}

			if (location.getText().contains(LOCATION_HELP)) {
				// locationData.setLatitude(participant.getLatitude());
				// locationData.setLongitude(participant.getLongitude());
				MainPageWidget.setErrorMessage("No location has been selceted");
			}

			service.filterList(locationData.getLatitude(), locationData
					.getLongitude(), location.getText(), locationData
					.getRadius(), hasSubs, needSubs,
					new AsyncCallback<SearchResponse>() {

						@Override
						public void onFailure(Throwable caught) {
							MainPageWidget
									.setErrorMessage("Zero results found!");
							showDefaultCursor();
						}

						@Override
						public void onSuccess(SearchResponse result) {
							sResponse = result;
							showSearchResults(sResponse);
							int size = sResponse.getAllResults().size();
							int km = sResponse.getAllResults().get(size - 1)
									.getDistanceInKm();
							mapUI.setZoomLevelToKm(km);
						}

					});
		} else if (event.getSource() == clearBtn) {
			clearSearchWidget();
		}

	}

	protected void showSearchResults(SearchResponse response) {
		int type = listBox.getSelectedIndex();
		showDefaultCursor();

		switch (type) {
		case 0:
			searchResponseWidget.setResults(response.getAllResults());
			mapUI.addPartMarkers(type, response.getAllResults());
			break;
		case 1:
			searchResponseWidget.setResults(response.getHas());
			mapUI.addPartMarkers(type, response.getHas());
			break;
		case 2:
			searchResponseWidget.setResults(response.getNeed());
			mapUI.addPartMarkers(type, response.getNeed());
			break;
		default:
			break;
		}

	}

	private void showDefaultCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}

	private void showWaitCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}

	@Override
	public void onChange(ChangeEvent event) {

	}

	public void clearSearchWidget() {
		subjectsSuggestWidget.clearAll();
		location.setText(LOCATION_HELP);
		listBox.setSelectedIndex(0);
		mapUI.clear();
	//	mapUI.map.addOverlay(mapUI.marker);
		mapUI.setMarkerLocation(LatLng.newInstance(17.45, 78.39, true)
				.getLatitude(), LatLng.newInstance(17.45, 78.39, true)
				.getLongitude(), 0);
		mapUI.map.setZoomLevel(4);
		searchResponseWidget.clearAll();

	}

}
