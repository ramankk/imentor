package com.bugyal.imentor.frontend.client;

import java.util.Arrays;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToMeWidget extends Composite {

	MentorServiceAsync service;

	FlexTable toMeList;
	VerticalPanel verticalPanel = new VerticalPanel();
	VerticalPanel contentPanel = new VerticalPanel();
	Button forwardButton = new Button(">");
	Button backwardButton = new Button("<");
	static int start = 0;
	static int end = 0;
	List<ParticipantVO> partsList;

	// Constructor for ToMeWidget
	public ToMeWidget(String EmailId) {

		service = (MentorServiceAsync) GWT.create(MentorService.class);

		initWidget(verticalPanel);
		verticalPanel.setSize("597px", "235px");

		Label lblToMe = new Label("To Me");
		lblToMe.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(lblToMe);
		lblToMe.setHeight("25px");

		contentPanel.setSize("600px", "180px");
		verticalPanel.add(contentPanel);
		DOM.setStyleAttribute(contentPanel.getElement(), "border",
				"1px solid #00f");
		end = start + 5;

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		horizontalPanel.add(backwardButton);
		backwardButton.setEnabled(false);

		horizontalPanel.add(forwardButton);

		getDataFeeds(EmailId);
	}

	// Method to Gett data from DataStore
	private void getDataFeeds(String EmailId) {
		service.feedToMe(EmailId, new AsyncCallback<List<ParticipantVO>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("ubanble to load");
			}

			@Override
			public void onSuccess(List<ParticipantVO> result) {
				callMe(result);
			}
		});
	}

	// For ForwardButtons and BackwardButtons
	private void callMe(final List<ParticipantVO> partsList) {
		this.partsList = partsList;

		backwardButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (start > 0) {
					contentPanel.clear();
					end = start;
					start = start - 5;
					fillMe();
				}
			}
		});

		forwardButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (end < partsList.size()) {
					contentPanel.clear();
					start = end;
					end = end + 5;
					fillMe();
				}
			}
		});
		fillMe();
	}

	// Filling ContentPanel with suitable records
	private void fillMe() {
		List<String> colors = (List<String>) Arrays.asList("#FFB90F",
				"#DAA520", "#FFC125", "#CD9B1D", "#EEB422"); // For ContentPanel Background Colour.
		int c = 0; // counter for Background color changing
		for (int i = start; i < end; i++) {
			ParticipantVO pv = partsList.get(i);

			Label nameFeed = new Label(pv.getName() + " ");
			Label dataFeed = new Label(" Looking for ");

			String needSub = " ";
			for (String str : pv.getNeedSubjects()) {
				needSub += str + ", ";
			}
			Label subjectsFeed = new Label(needSub + " ");
			HTML closeButton = new HTML("<a>X</a>");

			HorizontalPanel hp = new HorizontalPanel();

			hp.setSize("600px", "30px");
			DOM.setStyleAttribute(hp.getElement(), "backgroundColor",
					(String) colors.get(c++));

			// Refer StyleSheet named "war/ToMeWidget.css"
			nameFeed.addStyleName("myLabelCSS");
			dataFeed.addStyleName("myLabelCSS");
			subjectsFeed.addStyleName("myLabelCSS");
			closeButton.addStyleName("closeCSS");

			hp.add(nameFeed);
			hp.add(dataFeed);
			hp.add(subjectsFeed);
			hp.add(closeButton);

			contentPanel.add(hp);
		}

		if (start != 0)
			backwardButton.setEnabled(true);
		else
			backwardButton.setEnabled(false);

		if (end == partsList.size())
			forwardButton.setEnabled(false);
		else
			forwardButton.setEnabled(true);
	}
}
