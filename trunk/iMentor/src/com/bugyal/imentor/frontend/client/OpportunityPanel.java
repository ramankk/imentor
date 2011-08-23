package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpportunityPanel extends Composite implements ClickHandler {
	private final SubjectsSuggestWidget subWidget = new SubjectsSuggestWidget(new ArrayList<String>());

	private final TextArea txtMessage = new TextArea();;
	private final TextArea tbLocation = new TextArea();
	private final TabPanel tabPanel = new TabPanel();

	private OpportunityVO showingOpportunity = null;

	private static MapUI mapUI;
	private MentorServiceAsync service;

	private Button btnCreate, btnClear;
	private LocationData lData = new LocationData();

	private MyOpportunitiesWidget myOppWidget;
	private MainPageWidget mainPage;

	private final AsyncCallback<List<String>> getSubjectsCallback = new AsyncCallback<List<String>>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fail to get Subjects list");
		}

		@Override
		public void onSuccess(List<String> result) {
			subWidget.addMoreSubjects(result);
		}
	};

	private final AsyncCallback<List<OpportunityVO>> getOpportuniesCallback = new AsyncCallback<List<OpportunityVO>>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Unable to get opportunity" + caught.getMessage());
		}

		@Override
		public void onSuccess(List<OpportunityVO> result) {
			addMyOpportunities(result);
		}
	};

	public OpportunityPanel(MainPageWidget mainPage) {
		this.mainPage = mainPage;

		myOppWidget = new MyOpportunitiesWidget(this);
		service = (MentorServiceAsync) GWT.create(MentorService.class);

		// TODO(raman,sridhar): Get Subjects only once per browser and resuse
		// it.. dont let every
		// widget fetch its own list of subjects.
		service.getSubjects(getSubjectsCallback);
		service.getOpportunitiesById(getOpportuniesCallback);
		
		VerticalPanel subjectsVertical = new VerticalPanel();
		subjectsVertical.add(new Label("Subjects"));
		subjectsVertical.add(subWidget);
	
		VerticalPanel messageVertical = new VerticalPanel();
		messageVertical.setSize("250px", "130px");
		messageVertical.add(new Label("Message"));
		messageVertical.add(txtMessage);
		txtMessage.setSize("210px", "100px");
		
		VerticalPanel locationVertical = new VerticalPanel();
		locationVertical.setSize("250px", "130px");
		locationVertical.add(new Label("Location"));
		locationVertical.add(tbLocation);
		tbLocation.setText(lData.getLocation());
		tbLocation.setSize("215px", "50px");
		tbLocation.setText("Please, Use the Map");
		btnClear = new Button("Clear");
		btnClear.addClickHandler(this);
		btnCreate = new Button("Save");
		btnCreate.addClickHandler(this);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(btnClear);
		hp.add(btnCreate);
		locationVertical.add(hp);
		
		HorizontalPanel topHorizontal = new HorizontalPanel();
		topHorizontal.setWidth("750px");
		topHorizontal.add(subjectsVertical);
		topHorizontal.add(messageVertical);
		topHorizontal.add(locationVertical);
		
		tabPanel.setSize("750px", "200px");
		
		
		tabPanel.add(topHorizontal,"Set Opportunity");
		tabPanel.selectTab(0);
		mapUI = new MapUI(false, tbLocation);
		TabPanel map = new TabPanel();
		map.add(mapUI, "Map View");
		map.selectTab(0);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(tabPanel);
		mainPanel.add(map);

		initWidget(mainPanel);
	}

	private void addMyOpportunities(List<OpportunityVO> myOpportunities) {
		if (myOpportunities.size() == 0) {
			tabPanel.add(new Label("No opportunity is created by you.. "),
					"My Opportunity");
		} else {
			myOppWidget.setOpportunities(myOpportunities);
			tabPanel.add(myOppWidget, "My Opportunity");
		}
	}

	public void showOpportunity(OpportunityVO o) {
		subWidget.clearAll();
		// TODO(Sridhar, Ravi): Propagate message to the datastore and get it
		// back, no more faking the message.
		txtMessage.setText("");
		tbLocation.setText(o.getLocString());
		mapUI.setMarkerLocation(o.getLatitude(), o.getLongitude());
		subWidget.clearAll();
		for (String sub : o.getSubjects()) {
			subWidget.add(sub);
		}
		tabPanel.selectTab(0);
		showingOpportunity = o;
	}

	@Override
	public void onClick(ClickEvent event) {
		lData = mapUI.getLocationDetails();
		if (event.getSource() == btnCreate) {
			if (!(subWidget.getSubjects().isEmpty())
					&& !(tbLocation.getText().contains("Please, Use the Map"))) {
				Long id = showingOpportunity == null ? null
						: showingOpportunity.getId();
				OpportunityVO oppVO = new OpportunityVO(id,
						subWidget.getSubjects(), 1, 0,
						lData.getLatitude(), lData.getLongitude(), 1,
						tbLocation.getText(), txtMessage.getText());

				if (id == null) {
					// Create mode.
					service.createOpportunity(oppVO,
							new AsyncCallback<OpportunityVO>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Sorry, Unble to Create the Opportunity "
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(OpportunityVO result) {
									clearOpportunity();
									service.getOpportunitiesById(getOpportuniesCallback);
								}

							});
				} else {
					// edit mode.
					service.updateOpportunity(oppVO, new AsyncCallback<OpportunityVO>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Sorry, Unble to Create the Opportunity "
									+ caught.getMessage());
						}

						@Override
						public void onSuccess(OpportunityVO result) {
							clearOpportunity();
							service.getOpportunitiesById(getOpportuniesCallback);
						}
					});
				}

			}
		}

		if (event.getSource() == btnClear) {
			clearOpportunity();
		}

	}

	protected void clearOpportunity() {
		subWidget.clearAll();
		tbLocation.setText("Please, Use the Map");
		txtMessage.setText("");
		showingOpportunity = null;
	}

	public void showOnMap(OpportunityVO o) {
		mapUI.setMarkerLocation(o.getLatitude(), o.getLongitude());
	}

	// TODO(Sridhar, Ravi): How to go to creat-new-oppporutnity after checking
	// my-list-of-opportunities.. ?!!
}