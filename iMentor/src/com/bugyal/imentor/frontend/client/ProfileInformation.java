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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileInformation extends DialogBox implements ClickHandler {

	VerticalPanel panel;
	MentorServiceAsync service;
	SearchResult result = null;	
	Button cancel, persue;
	
	public ProfileInformation(SearchResult results) {
		setHTML("Profile Info");
		
		panel = new VerticalPanel();
		setWidget(panel);
		panel.setSize("400px", "230px");
		
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		
		if(results != null){
			panel.add(new HTML("<b>Name :</b>"+results.getP().getName()));
			panel.add(new HTML("<b>Gender :</b>"+results.getP().getGender()));
			panel.add(new HTML("<b>Email Id :</b>"+results.getP().getEmail()));
			panel.add(new HTML("<b>Location :</b>"+results.getP().getLocationString()));
			
			StringBuilder haslist = new StringBuilder();
			for(String s: results.getP().getHasSubjects() ) {
				haslist.append("  " + s);
			}
			
			StringBuilder needlist = new StringBuilder();
			for(String s: results.getP().getNeedSubjects() ) {
				needlist.append("  " + s);
			}
			
			panel.add(new HTML("<b>Has Subjects :</b>"+haslist.toString()));
			panel.add(new HTML("<b>Need Subjects :</b>"+needlist.toString()));
			
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
						
						panel.add(new HTML("<b>Mentors List :</b>"+mentorlist.toString()));
						panel.add(new HTML("<b>Mentees List :</b>"+menteelist.toString()));
					}	
					
				}
				
			};
			service.getMentorAndMentees(results.getP(), callback1);	
			
		}
		
		cancel = new Button("Cancel");
		persue = new Button("Persue");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(cancel);
		hp.add(persue);
		panel.add(hp);
		panel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		panel.setSize("300px", "300px");
		setWidget(panel);
		
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

}
