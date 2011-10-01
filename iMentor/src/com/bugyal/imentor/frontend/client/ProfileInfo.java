package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.shared.MentorDataStatus;
import com.bugyal.imentor.frontend.shared.MentorsResult;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileInfo extends DialogBox implements ClickHandler {
	VerticalPanel vp = new VerticalPanel();
	Button cancel, pursue;
	MentorServiceAsync service;
	SearchResult result = null;
	SearchResponseWidget widget;
	FlexTable table = null;
	boolean isPersue;
	long participantId;

	public ProfileInfo(SearchResponseWidget widget, SearchResult results) {
		this.widget = widget;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		this.result = results;
		setHTML("Profile Info");
		table = new FlexTable();

		if (results != null) {
			participantId = results.getP().getId();

			setData(0, "Name", results.getP().getName());
			setData(1, "Gender", results.getP().getGender());
			setData(2, "Email Id", results.getP().getEmail());
			setData(3, "Location", results.getP().getLocationString());
			setData(4, "Has Subjects", results.getP().getHasSubjectsAsString());
			setData(5, "Need Subjects", results.getP()
					.getNeedSubjectsAsString());

			AsyncCallback<MentorDataStatus> callback1 = new AsyncCallback<MentorDataStatus>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(MentorDataStatus data) {
					if (data.isExisted()) {
						pursue.setText("Delete");
						isPersue = data.isExisted();
					} else {
						isPersue = data.isExisted();
					}
					if (data.getMentorslist().size() != 0) {
						StringBuilder mentorlist = new StringBuilder();
						StringBuilder menteelist = new StringBuilder();
						boolean firstMentor = true;
						boolean firstMentee = true;
						for (MentorsResult s : data.getMentorslist()) {
							if (s.isMentor()) {
								if (firstMentor) {
									firstMentor = false;
								} else {
									mentorlist.append(", ");
								}
								mentorlist.append(s.getName());
							} else {
								if (firstMentee) {
									firstMentee = false;
								} else {
									menteelist.append(", ");
								}
								menteelist.append(s.getName());
							}
						}
						if (mentorlist.length() == 0) {
							mentorlist.append("  ----");
						}
						if (menteelist.length() == 0) {
							menteelist.append("  ----");
						}
						setData(6, "Mentors List ", mentorlist.toString());
						setData(7, "Mentees List ", menteelist.toString());
					}
				}
			};
			service.getMentorAndMentees(results.getP(), callback1);
		}

		vp.add(table);
		cancel = new Button("Cancel");
		pursue = new Button("Pursue");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(cancel);
		hp.add(pursue);
		vp.add(hp);

		vp.setSize("300px", "250px");
		setWidget(vp);

		cancel.addClickHandler(this);
		pursue.addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {

		if (event.getSource() == cancel) {
			this.hide();
		}

		if (event.getSource() == pursue) {
			if (!isPersue) { // To add mentor or mentee to users profile
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						MainPageWidget.setErrorMessage("Failed to add");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							MainPageWidget.setMessage("Successfully added");
						} else {
							MainPageWidget.setErrorMessage("Failed to add");
						}
					}
				};
				if (result.isTypeParticipant()) {
					service.addMentorAndMentee(result.isHas(), result.getP()
							.getEmail(), callback);
				} else {
					Window.alert("Not a participant");
				}
				this.hide();
			} else { // To remove mentor or mentee from users profile
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to add mentor to mentee");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Window
									.alert("Successfully mentor or mentee deleted");
							reloadWidget();
						} else {
							Window.alert("Failed to delete mentor to mentee");
						}
					}
				};
				if (result.isTypeParticipant()) {
					service.deleteMentorOrMentee(result.isHas(), result.getP()
							.getEmail(), callback);
				} else {
					Window.alert("Not a participant");
				}
				this.hide();
			}

		}
	}

	public void reloadWidget() {
		widget.filsterList(result);
	}

	void setData(int index, String field, String value) {
		table.setWidget(index, 0, new HTML("<b>" + field + " </b>"));
		table.getCellFormatter().setWidth(index, 0, "100px");
		// table.setWidget(index, 0, new Label(field + " : "));
		table.setWidget(index, 1, new Label(": " + value));
	}

}
