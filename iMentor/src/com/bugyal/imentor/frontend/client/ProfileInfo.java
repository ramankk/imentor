package com.bugyal.imentor.frontend.client;

import java.util.List;

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

	public ProfileInfo(SearchResult results) {

		service = (MentorServiceAsync) GWT.create(MentorService.class);
		this.result = results;		
		setHTML("Profile Info");
		table = new FlexTable();
		
		if(results != null){
			setData(0, "Name", results.getP().getName());
			setData(1, "Gender", results.getP().getGender());
			setData(2, "Email Id", results.getP().getEmail());
			setData(3, "Location", results.getP().getLocationString());
			StringBuilder haslist = new StringBuilder();
			for(String s: results.getP().getHasSubjects() ) {
				haslist.append("  " + s);
			}
			setData(4, "Has Subjects", haslist.toString());
			
			StringBuilder needlist = new StringBuilder();
			for(String s: results.getP().getNeedSubjects() ) {
				needlist.append("  " + s);
			}
			setData(5, "Need Subjects", needlist.toString());
			
			AsyncCallback<List<MentorsResult>> callback1 = new AsyncCallback<List<MentorsResult>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(List<MentorsResult> result) {
					if(result.size() != 0) {
						StringBuilder mentorlist = new StringBuilder();
						StringBuilder menteelist = new StringBuilder();
						
						for(MentorsResult s: result) {
							if(s.isMentor()){
								mentorlist.append("  " + s.getName());
							}
							else{
								menteelist.append(" " + s.getName());
							}
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
		persue = new Button("Persue");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(cancel);
		hp.add(persue);
		vp.add(hp);

		vp.setSize("280px", "250px");
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
			this.hide();
		}		
	}
	
	void setData(int index, String field, String value) {
			table.setWidget(index, 0, new Label(field + " : "));
			table.setWidget(index, 1, new Label(value));
	}

}
