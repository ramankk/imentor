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
	Button searchBtn;
	MentorServiceAsync service;
	LocationData locationData;
	TextArea location;
	SubjectsSuggestWidget subjectsSuggestWidget;
	ListBox listBox;
	SearchResponse sResponse;
	TabPanel tabPanel_1 = new TabPanel();
	TabPanel switchView = new TabPanel();

	SearchResponseWidget searchResultsWidget = new SearchResponseWidget();

	FlexTable resultsPanel = new FlexTable();

	public SearchWidget() {
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		location = new TextArea();
		location.setTitle("finds results on this Location");
		mapUI = new MapUI(true, location);		
		subjectsSuggestWidget = new SubjectsSuggestWidget(new ArrayList<String>());
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
		
		VerticalPanel subjectsVertical = new VerticalPanel();
		subjectsVertical.setHeight("80px");
		subjectsVertical.add(new Label("Select Subjects"));
		subjectsVertical.add(subjectsSuggestWidget);
		
		location = new TextArea();
		location.setText("Please, Use the Map");
		location.setEnabled(false);
		location.setSize("213px", "43px");
		
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
		listBox.addItem("Mentee/opp");
		HorizontalPanel dropdownHorizontal = new HorizontalPanel();
		dropdownHorizontal.add(new Label("Search By:"));
		dropdownHorizontal.add(listBox);
		
		searchBtn = new Button("Search");
		searchBtn.addClickHandler(this);
		
		searchVertical.add(dropdownHorizontal);
		searchVertical.add(searchBtn);
		searchVertical.setCellHorizontalAlignment(searchBtn, HasHorizontalAlignment.ALIGN_RIGHT);
		searchVertical.setHeight("80px");
		
		HorizontalPanel topHorizontal = new HorizontalPanel();
		topHorizontal.add(subjectsVertical);
		topHorizontal.add(locationVertical);
		topHorizontal.add(searchVertical);
		topHorizontal.setWidth("700px");
		tabPanel_1.add(topHorizontal, "Search");
		tabPanel_1.selectTab(0);
		tabPanel_1.setSize("700px", "150px");
		switchView.add(mapUI,"Map View");
		switchView.add(searchResultsWidget,"List View");
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
	}

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
