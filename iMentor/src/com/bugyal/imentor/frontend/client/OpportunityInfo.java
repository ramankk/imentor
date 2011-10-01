package com.bugyal.imentor.frontend.client;

import java.util.List;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpportunityInfo extends DialogBox implements ClickHandler{


	VerticalPanel vp = new VerticalPanel();	
	Button cancel, pursue;
	MentorServiceAsync service;
	SearchResult result = null;
	SearchResponseWidget widget = null;
	FlexTable table=null;
	boolean isPersue;
	
	public OpportunityInfo(SearchResponseWidget widget, SearchResult results) {
		this.widget = widget;
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		this.result = results;		
		setHTML("Opportunity Info");
		table = new FlexTable();
		
		if(results != null){
			setData(0, "Need Subject(s)", results.getO().getSubjectsAsString());
			setData(1, "Message", results.getO().getMessage());
			setData(2, "Location", results.getO().getLocString());
			
			AsyncCallback<List<MentorsResult>> callback = new AsyncCallback<List<MentorsResult>>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub					
				}
				
				@Override
				public void onSuccess(List<MentorsResult> result) {					
					if(result.size() != 0) {
						StringBuilder ownerslist = new StringBuilder();	
						boolean first = true;
						for(MentorsResult s: result) {
							if(!s.isMentor()) {
								if (first) {
									first = false;
								} else {
									ownerslist.append(", ");
								}
								ownerslist.append(s.getName());
							}
						}
						setData(3, "Owners", ownerslist.toString());						
					}					
				}
			};				
			service.searchOwnersById(results.getO().getId(), callback);	
			
			AsyncCallback<MentorDataStatus> callback1 = new AsyncCallback<MentorDataStatus>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub					
				}

				@Override
				public void onSuccess(MentorDataStatus data) {
					if(data.isExisted()) {						
						pursue.setText("Delete");
						isPersue = data.isExisted();
					} else {
						isPersue = data.isExisted();
					}
					if(data.getMentorslist().size() != 0) {
						StringBuilder mentorlist = new StringBuilder();
						boolean first = true;
						for(MentorsResult s: data.getMentorslist()) {
							if(s.isMentor()){
								if(first) {
									first = false;
								} else {
									mentorlist.append(", ");
								}
								mentorlist.append(s.getName());
							}
						}
						setData(4, "Mentors List ", mentorlist.toString());
					}					
				}
			};			
			service.getMentorsForOpportunity(results.getO().getId(), callback1);					
		}		
		vp.add(table);
		cancel = new Button("Cancel");
		pursue = new Button("Pursue");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(cancel);
		hp.add(pursue);
		vp.add(hp);

		vp.setSize("270px", "250px");
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
			if(!isPersue) { 	// To add opportunity to the user 
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to add mentor to Opportunity");				
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Window.alert("Successfully mentor added to Opportunity");
						} else {
							Window.alert("Failed to add mentor to Opportunity");
						}				
					}
				};
				if(!result.isTypeParticipant()){
					service.addMentorToOpportunity(result.getO().getId(), callback);
				}
				else{
					Window.alert("Not a participant");
				}
				this.hide();
			} else {		// To delete opportunity from the user 
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to delete mentor of Opportunity");				
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Window.alert("Successfully mentor deleted for Opportunity");
							reloadWidget();
						} else {
							Window.alert("Failed to delete mentor of Opportunity");
						}				
					}
				};
				if(!result.isTypeParticipant()){
					service.removeMentorForOpportunity(result.getO().getId(), callback);
				}
				else{
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
			table.setWidget(index, 0, new Label(field + " : "));
			table.setWidget(index, 1, new Label(value));
	}



}
