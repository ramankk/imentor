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
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpportunityDialogBox extends DialogBox implements ClickHandler {
	private final SubjectsSuggestWidget subWidget = new SubjectsSuggestWidget();

	private final TextArea txtMessage = new TextArea();;
	private final TextArea tbLocation = new TextArea();
	private final StackPanel stackPanel = new StackPanel();

	private OpportunityVO showingOpportunity = null;

	private static MapUI mapUI;
	private MentorServiceAsync service;

	private Button btnCreate, btnCancel, btnClear;
	private LocationData lData = new LocationData();
	private String emailId;

	private MyOpportunitiesWidget myOppWidget;

	private final AsyncCallback<List<String>> getSubjectsCallback = new AsyncCallback<List<String>>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fail to get Subjects list");
		}

		@Override
		public void onSuccess(List<String> result) {
			Window.alert("success to get Subjects list");
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
			Window.alert("Success to get Subjects list");
			addMyOpportunities(result);
		}
	};

	public OpportunityDialogBox() {
		myOppWidget = new MyOpportunitiesWidget(this);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		setSize("400px", "608px");
		setHTML("Opportunity");
		emailId = "sridhar@kawa";

		// TODO(raman,sridhar): Get Subjects only once per browser and resuse
		// it.. dont let every
		// widget fetch its own list of subjects.
		Window.alert("getsub");
		service.getSubjects(getSubjectsCallback);
		Window.alert("getOpp");
		service.getOpportunitiesById(emailId, getOpportuniesCallback);

		setWidget(horizontalPanel);
		horizontalPanel.setSize("750px", "558px");

		VerticalPanel verticalPanel = new VerticalPanel();
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

		verticalPanel.add(txtMessage);
		txtMessage.setSize("245px", "90px");

		btnClear = new Button("Clear");
		btnClear.addClickHandler(this);

		btnCreate = new Button("Save");
		btnCreate.addClickHandler(this);

		btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(this);

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(btnClear);
		hp.add(btnCreate);
		hp.add(btnCancel);
		verticalPanel.add(hp);

		stackPanel.add(verticalPanel, "Set Opportunity");

		horizontalPanel.add(stackPanel);
		mapUI = new MapUI(false, tbLocation);
		mapUI.setWidth("500px");
		horizontalPanel.add(mapUI);
	}

	private void addMyOpportunities(List<OpportunityVO> myOpportunities) {
		if (myOpportunities.size() == 0) {
			Window.alert(" " + myOpportunities.size());
			stackPanel.add(new Label("No opportunity is created by you.. "),
					"My Opportunity");
		} else {
			Window.alert(" " + myOpportunities.size());
			myOppWidget.setOpportunities(myOpportunities);
			stackPanel.add(myOppWidget, "My Opportunity");
		}
	}

	public void showOpportunity(OpportunityVO o) {
		subWidget.selected.clearAll();
		// TODO(Sridhar, Ravi): Propagate message to the datastore and get it
		// back, no more faking the message.
		txtMessage.setText("");
		tbLocation.setText(o.getLocString());
		mapUI.setMarkerLocation(o.getLatitude(), o.getLongitude());
		subWidget.selected.clearAll();
		for (String sub : o.getSubjects()) {
			subWidget.selected.add(sub);
		}
		stackPanel.showStack(0);
		showingOpportunity = o;
	}

	@Override
	public void onClick(ClickEvent event) {
		lData = mapUI.getLocationDetails();
		if (event.getSource() == btnCreate) {
			if (!(subWidget.selected.getSubjects().isEmpty())
					&& !(tbLocation.getText().contains("Please, Use the Map"))) {
				Window.alert("form validated !! ");
				Long id = showingOpportunity == null ? null
						: showingOpportunity.getId();
				OpportunityVO oppVO = new OpportunityVO(id,
						subWidget.selected.getSubjects(), 1, 0,
						lData.getLatitude(), lData.getLongitude(), 1,
						tbLocation.getText(), txtMessage.getText());

				if (id == null) {
					// Create mode.
					Window.alert("new Opportunity !! ");
					service.createOpportunity(emailId, oppVO,
							new AsyncCallback<OpportunityVO>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Sorry, Unble to Create the Opportunity "
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(OpportunityVO result) {
									Window.alert("You Have successfully created an Opportunity");
									clearOpportunity();
									hideOpportunityDialogBox();
									service.getOpportunitiesById(emailId,
											getOpportuniesCallback);
								}

							});
				} else {
					// edit mode.
					Window.alert("update Opportunity !! ");
					service.updateOpportunity(oppVO, emailId,
							new AsyncCallback<OpportunityVO>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Sorry, Unble to Create the Opportunity "
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(OpportunityVO result) {
									Window.alert("You Have successfully updated an Opportunity");
									clearOpportunity();
									hideOpportunityDialogBox();
									service.getOpportunitiesById(emailId,
											getOpportuniesCallback);
								}
							});
				}

			}
		}

		if (event.getSource() == btnCancel) {
			clearOpportunity();
			hideOpportunityDialogBox();
		}

		if (event.getSource() == btnClear) {
			clearOpportunity();
		}

	}

	protected void clearOpportunity() {
		subWidget.selected.clearAll();
		tbLocation.setText("Please, Use the Map");
		txtMessage.setText("");
		showingOpportunity = null;
	}

	protected void hideOpportunityDialogBox() {
		this.hide();
	}

	public void showOnMap(OpportunityVO o) {
		mapUI.setMarkerLocation(o.getLatitude(), o.getLongitude());
	}

	// TODO(Sridhar, Ravi): How to go to creat-new-oppporutnity after checking
	// my-list-of-opportunities.. ?!!
}