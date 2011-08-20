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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
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
	RadioButton rbtnAll, rbtnMentor, rbtnMentee;
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
			}});
		return viewToggler;
	}

	private HorizontalPanel buildSearchForm() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSize("750px", "317px");

		VerticalPanel filterPanel = new VerticalPanel();
		horizontalPanel.add(filterPanel);
		filterPanel.setSize("255px", "315px");

		Label label = new Label("Select Subjects :");
		filterPanel.add(label);

		subjectsSuggestWidget = new SubjectsSuggestWidget();

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
		subjectsSuggestWidget.suggestBox.setWidth("243px");
		filterPanel.add(subjectsSuggestWidget);
		subjectsSuggestWidget.setSize("250px", "75px");

		Label label_1 = new Label("Select Location:");
		filterPanel.add(label_1);

		location = new TextArea();
		filterPanel.add(location);
		location.setText("Please, Use the Map");
		location.setEnabled(false);
		location.setSize("243px", "75px");

		searchBtn = new Button("Search");
		searchBtn.addClickHandler(this);
		filterPanel.add(searchBtn);
		filterPanel.setCellHorizontalAlignment(searchBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel verticalPanel_2 = new VerticalPanel();
		horizontalPanel.add(verticalPanel_2);
		verticalPanel_2.setSize("483px", "314px");

		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel_2.add(horizontalPanel_3);
		horizontalPanel_3.setSize("491px", "33px");

		Label lblNewLabel = new Label("Search By:");
		horizontalPanel_3.add(lblNewLabel);
		horizontalPanel_3.setCellVerticalAlignment(lblNewLabel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_3.setCellHorizontalAlignment(lblNewLabel,
				HasHorizontalAlignment.ALIGN_CENTER);

		rbtnAll = new RadioButton("new name", "All");
		rbtnAll.addClickHandler(this);
		horizontalPanel_3.add(rbtnAll);
		horizontalPanel_3.setCellVerticalAlignment(rbtnAll,
				HasVerticalAlignment.ALIGN_MIDDLE);

		rbtnMentor = new RadioButton("new name", "Mentor");
		rbtnMentor.addClickHandler(this);
		horizontalPanel_3.add(rbtnMentor);
		horizontalPanel_3.setCellVerticalAlignment(rbtnMentor,
				HasVerticalAlignment.ALIGN_MIDDLE);

		rbtnMentee = new RadioButton("new name", "Mentee & Oppurtunities");
		rbtnMentee.addClickHandler(this);
		horizontalPanel_3.add(rbtnMentee);
		horizontalPanel_3.setCellVerticalAlignment(rbtnMentee,
				HasVerticalAlignment.ALIGN_MIDDLE);

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

			service.filterList(locationData.getLatitude(),
					locationData.getLongitude(), location.getText(),
					locationData.getRadius(), hasSubs, needSubs,
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

		} else if (event.getSource() == rbtnMentor) {
			searchResultsWidget.setResults(sResponse.getHas());
		} else if (event.getSource() == rbtnMentee) {
			searchResultsWidget.setResults(sResponse.getNeed());
		} else {
			searchResultsWidget.setResults(sResponse.getAllResults());
		}
	}

	@SuppressWarnings("deprecation")
	protected void showSearchResults(SearchResponse response) {
		List<ParticipantVO> pList = new ArrayList<ParticipantVO>();
		searchResultsWidget.setResults(response.getAllResults());

		for (SearchResult p : response.getHas()) {
			pList.add(p.getP());
		}
		showDefaultCursor();
		rbtnAll.setChecked(true);
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
