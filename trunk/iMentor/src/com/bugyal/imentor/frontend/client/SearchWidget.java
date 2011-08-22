package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchWidget extends Composite implements ClickHandler,
		ChangeHandler {
	MapUI mapUI;
	Button searchBtn;
	MentorServiceAsync service;
	LocationData locationData;
	TextArea location;
	SubjectsSuggestWidget subjectsSuggestWidget;
	ListBox listBox;
	SearchResponse sResponse;

	SearchResponseWidget searchResultsWidget = new SearchResponseWidget();

	FlexTable resultsPanel = new FlexTable();

	public SearchWidget() {
		service = (MentorServiceAsync) GWT.create(MentorService.class);

		mapUI = new MapUI(true, location);
		mapUI.setWidth("600px");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSize("750px", "756px");
		mainPanel.add(buildSearchForm());
		mainPanel.add(buildViewToggler());
		mainPanel.add(resultsPanel);

		showMapView();
		locationData = mapUI.getLocationDetails();

		initWidget(mainPanel);
	}

	public void showListView() {
		isMapViewDisplayed = false;
		viewToggler.setText(MAP_VIEW);
		resultsPanel.setWidget(0, 0, searchResultsWidget);
	}

	public void showMapView() {
		isMapViewDisplayed = true;
		viewToggler.setText(LIST_VIEW);
		resultsPanel.setWidget(0, 0, mapUI);
	}

	private static final String LIST_VIEW = "List View";
	private static final String MAP_VIEW = "Map View";
	private boolean isMapViewDisplayed = true;
	private final Button viewToggler = new Button(LIST_VIEW);

	private Button buildViewToggler() {

		viewToggler.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isMapViewDisplayed) {
					showListView();
				} else {
					showMapView();
				}
			}
		});
		return viewToggler;
	}

	private HorizontalPanel buildSearchForm() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSize("750px", "317px");

		VerticalPanel subjectPanel = new VerticalPanel();
		VerticalPanel locationPanel = new VerticalPanel();

		Label subLabel = new Label("Select Subjects :");
		subjectsSuggestWidget = new SubjectsSuggestWidget(
				new ArrayList<String>());

		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail to get Subjects list");
			}

			@Override
			public void onSuccess(List<String> result) {
				subjectsSuggestWidget.addMoreSubjects(result);
			}

		};
		service.getSubjects(callback);
		// subjectsSuggestWidget.suggestBox.setWidth("243px");
		// subjectsSuggestWidget.setSize("250px", "75px");

		subjectPanel.add(subLabel);
		subjectPanel.add(subjectsSuggestWidget);
		// subjectPanel.setSize("255px", "315px");

		Label locLabel = new Label("Select Location:");
		location = new TextArea();
		location.setText("Please, Use the Map");
		location.setEnabled(false);
		location.setSize("243px", "75px");

		locationPanel.add(locLabel);
		locationPanel.add(location);

		HorizontalPanel dropDown = new HorizontalPanel();

		dropDown.add(new Label("Search By:"));
		listBox = new ListBox();
		listBox.addItem("All");
		listBox.addItem("Mentors");
		listBox.addItem("Mentee/opp");
		dropDown.add(listBox);
		searchBtn = new Button("Search");
		searchBtn.addClickHandler(this);
		dropDown.add(searchBtn);

		locationPanel.add(dropDown);

		horizontalPanel.add(subjectPanel);
		horizontalPanel.add(locationPanel);

		VerticalPanel verticalPanel_2 = new VerticalPanel();
		horizontalPanel.add(verticalPanel_2);
		verticalPanel_2.setSize("483px", "314px");

		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel_2.add(horizontalPanel_3);
		horizontalPanel_3.setSize("491px", "33px");

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		verticalPanel_2.add(horizontalPanel_2);
		verticalPanel_2.setCellHorizontalAlignment(horizontalPanel_2,
				HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel_2.setSize("57px", "29px");

		return horizontalPanel;
	}

	@Override
	public void onClick(ClickEvent event) {

		List<String> hasSubs, needSubs;

		// TODO(sundeep): Fix me, pick up the participant's has and need
		// subjects
		// when user doesn't supply them.
		hasSubs = new ArrayList<String>();
		needSubs = new ArrayList<String>();

		if (event.getSource() == searchBtn) {
			showWaitCursor();
			locationData = mapUI.getLocationDetails();

			if (subjectsSuggestWidget.getSelected().isEmpty()) {
				// hasSubs = ;(get from logged in user TODO Sundeep)
				// needSubs = ;
			} else {
				hasSubs = subjectsSuggestWidget.getSelected();
				needSubs = subjectsSuggestWidget.getSelected();
			}

			if (location.getText().contains("Please, Use the Map")) {
				location.setText("");// get from logged in user TODO Sundeep
			}

			service.filterList(locationData.getLatitude(), locationData
					.getLongitude(), location.getText(), locationData
					.getRadius(), hasSubs, needSubs,
					new AsyncCallback<SearchResponse>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Unable to load");
						}

						@Override
						public void onSuccess(SearchResponse result) {
							sResponse = result;
							showSearchResults(sResponse);

						}

					});
		}
		// } else if (event.getSource() == rbtnMentor) {
		// searchResultsWidget.setResults(sResponse.getHas());
		// } else if (event.getSource() == rbtnMentee) {
		// searchResultsWidget.setResults(sResponse.getNeed());
		// } else {
		// searchResultsWidget.setResults(sResponse.getAllResults());
		// }
	}

	@SuppressWarnings("deprecation")
	protected void showSearchResults(SearchResponse response) {
		List<ParticipantVO> pList = new ArrayList<ParticipantVO>();
		searchResultsWidget.setResults(response.getAllResults());

		for (SearchResult p : response.getHas()) {
			pList.add(p.getP());
		}
		showDefaultCursor();
		listBox.setSelectedIndex(0);
		mapUI.addPartMarkers(1, pList);
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

}
