package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileInfo extends DialogBox implements ClickHandler {
	VerticalPanel vp = new VerticalPanel();
	Label nameLabel, name;
	Label genderLabel, gender;
	HorizontalPanel nameHP, genderHP;
	Button cancel, persue;
	MentorServiceAsync service;
	SearchResult result = null;
	FlexTable table=null;

	public ProfileInfo(SearchResult result) {

		service = (MentorServiceAsync) GWT.create(MentorService.class);
		this.result = result;		
		setHTML("Profile Info");
		table = new FlexTable();
		if(result != null){
			setData(0, "Name", result.getP().getName());
			setData(1, "Gender", result.getP().getGender());
			setData(2, "Email Id", result.getP().getEmail());
			setData(3, "Location", result.getP().getLocationString());
			setData(4, "Has Subjects", "Has list");
			setData(5, "Need Subjects", "Need list");
			//setData(5, "Mentors ", "Mentors list");
			//setData(5, "Mentees", "Mentees list");
		}
		
		vp.add(table);
		cancel = new Button("Cancel");
		persue = new Button("Persue");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(cancel);
		hp.add(persue);
		vp.add(hp);

		vp.setSize("200px", "150px");
		setWidget(vp);

		cancel.addClickHandler(this);
		persue.addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		
		if (event.getSource() == cancel) {
			this.hide();
		}
		
		if (event.getSource() == persue) {
			
			Window.alert("Persue button clicked");
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed to add mentor to mentee");
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						Window.alert("Successfully mentor added to mentee");
					} else {
						Window.alert("Failed to add mentor to mentee");
					}
				}
			};
			if(result.isTypeParticipant()){
				service.addMentorAndMentee(result.isHas(), result.getP().getEmail(), callback);
			}
			else{
				Window.alert("Not a participant");
			}
		}
	}
	
	void setData(int index, String field, String value) {
			table.setWidget(index, 0, new Label(field + " : "));
			table.setWidget(index, 1, new Label(value));
	}

}
