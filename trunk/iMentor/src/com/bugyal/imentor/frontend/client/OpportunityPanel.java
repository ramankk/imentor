package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpportunityPanel extends Composite implements ClickHandler {
	private final SubjectsSuggestWidget subWidget = new SubjectsSuggestWidget(
			new ArrayList<String>());

	private final TextArea txtMessage = new TextArea();
	private final TextArea tbLocation = new TextArea();
	private final TabPanel tabPanel = new TabPanel();

	private OpportunityVO showingOpportunity = null;

	private static MapUI mapUI;
	private MentorServiceAsync service;

	private Button btnCreate, btnCancel;
	private LocationData lData = new LocationData();

	private MyOpportunitiesWidget myOppWidget;
	private MainPageWidget mainPage;

	private final AsyncCallback<List<String>> getSubjectsCallback = new AsyncCallback<List<String>>() {

		@Override
		public void onFailure(Throwable caught) {
			// mainPage.setErrorMessage("Fail to get Subjects list");
		}

		@Override
		public void onSuccess(List<String> result) {
			subWidget.addMoreSubjects(result);
		}
	};

	private final AsyncCallback<List<OpportunityVO>> getOpportuniesCallback = new AsyncCallback<List<OpportunityVO>>() {

		@Override
		public void onFailure(Throwable caught) {
			mainPage.setErrorMessage("Unable to get opportunity"
					+ caught.getMessage());
		}

		@Override
		public void onSuccess(List<OpportunityVO> result) {
			addMyOpportunities(result);
		}
	};

	private final AsyncCallback<Boolean> deleteOpportunityCallback = new AsyncCallback<Boolean>() {

		@Override
		public void onFailure(Throwable caught) {
			mainPage.setMessage("Sorry, Unable to delete the Opportunity"
					+ caught.getMessage());
		}

		@Override
		public void onSuccess(Boolean result) {
			mainPage.setMessage("Opportunity has been deleted Successfully");
		}
	};

	public OpportunityPanel(MainPageWidget mainPage) {
		this.mainPage = mainPage;

		myOppWidget = new MyOpportunitiesWidget(this);
		service = (MentorServiceAsync) GWT.create(MentorService.class);

		service.getSubjects(getSubjectsCallback);

		VerticalPanel subjectsVertical = new VerticalPanel();
		subjectsVertical.add(new Label("Subjects"));
		subjectsVertical.add(subWidget);

		VerticalPanel messageVertical = new VerticalPanel();
		messageVertical.add(new Label("Message:"));
		messageVertical.add(txtMessage);

		txtMessage.setSize("185px", "45px");

		VerticalPanel locationVertical = new VerticalPanel();

		locationVertical.add(new Label("Location:"));
		locationVertical.add(tbLocation);
		tbLocation.setSize("185px", "45px");
		tbLocation.setText("Please, Use the Map");

		HorizontalPanel buttonPanel = new HorizontalPanel();

		btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(this);
		btnCreate = new Button("Save");
		btnCreate.addClickHandler(this);
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnCreate);
		locationVertical.add(buttonPanel);

		locationVertical.setCellHorizontalAlignment(buttonPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel topHorizontal = new HorizontalPanel();
		topHorizontal.setWidth("700px");
		topHorizontal.add(subjectsVertical);
		topHorizontal.add(messageVertical);
		topHorizontal.add(locationVertical);

		tabPanel.setSize("690px", "150px");

		tabPanel.add(topHorizontal, "Create Opportunity");
		tabPanel.getTabBar().setTabText(1, "test");
		tabPanel.selectTab(0);
		mapUI = new MapUI(false, tbLocation);
		TabPanel map = new TabPanel();
		map.add(mapUI, "Map View");
		map.selectTab(0);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(tabPanel);
		mainPanel.add(map);

		txtMessage
				.setTitle("useful to know more about \n the Opportunity and contact Details");
		tbLocation.setTitle("used to find members of this Location");
		initWidget(mainPanel);

		tabPanel.getTabBar().addSelectionHandler(new SelectionHandler() {

			@Override
			public void onSelection(SelectionEvent event) {
				if (event.getSelectedItem() == (Object) 1) {
					tabPanel.getTabBar().setTabText(0, "Create Opportunity");
					clearOpportunity();
				}
			}

		});
	}

	public void deleteOpportunityById(long id) {
		service.deleteOpportunity(id, deleteOpportunityCallback);
	}

	public void getOpportunitiesById() {
		service.getOpportunitiesById(getOpportuniesCallback);
	}

	private void addMyOpportunities(List<OpportunityVO> myOpportunities) {
		if (myOpportunities.size() == 0) {
			if (tabPanel.getWidgetCount() > 1) {
				tabPanel.remove(1);
			}
			tabPanel.add(new Label("No opportunity is created by you.. "),
					"My Opportunity");
		} else {
			myOppWidget.setOpportunities(myOpportunities);
			if (tabPanel.getWidgetCount() > 1) {
				tabPanel.remove(1);
			}
			tabPanel.add(myOppWidget, "My Opportunity");
			tabPanel.selectTab(1);
		}
	}

	public void showOpportunity(OpportunityVO o) {
		clearOpportunity();
		txtMessage.setText(o.getMessage());
		tbLocation.setText(o.getLocString());
		// mapUI.setMarkerLocation(o.getLatitude(), o.getLongitude());

		for (String sub : o.getSubjects()) {
			subWidget.add(sub);
		}
		tabPanel.getTabBar().setTabText(0, "Edit Opportunity");
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
				OpportunityVO oppVO = new OpportunityVO(id, subWidget
						.getSubjects(), 1, 0, lData.getLatitude(), lData
						.getLongitude(), 1, tbLocation.getText(), txtMessage
						.getText(), 0);

				if (id == null) {
					// Create mode.
					service.createOpportunity(oppVO,
							new AsyncCallback<OpportunityVO>() {

								@Override
								public void onFailure(Throwable caught) {
									mainPage
											.setErrorMessage("Sorry, Unble to Create the Opportunity "
													+ caught.getMessage());
								}

								@Override
								public void onSuccess(OpportunityVO result) {
									clearOpportunity();
									service
											.getOpportunitiesById(getOpportuniesCallback);
									mainPage
											.setMessage("Opportunity has been created successfully");
								}

							});
				} else {
					// edit mode.
					service.updateOpportunity(oppVO,
							new AsyncCallback<OpportunityVO>() {

								@Override
								public void onFailure(Throwable caught) {
									mainPage
											.setErrorMessage("Sorry, Unble to Create the Opportunity "
													+ caught.getMessage());
								}

								@Override
								public void onSuccess(OpportunityVO result) {
									clearOpportunity();
									service
											.getOpportunitiesById(getOpportuniesCallback);
									mainPage
											.setMessage("Opportunity updated successfully");
									tabPanel.getTabBar().setTabText(0,
											"Create Opportunity ");
								}
							});
				}

			} else {
				mainPage.setErrorMessage("Fields are empty");
			}
		} else if (event.getSource() == btnCancel) {
			clearOpportunity();
			tabPanel.getTabBar().setTabText(0, "Create Opportunity");
			tabPanel.selectTab(1);
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

}