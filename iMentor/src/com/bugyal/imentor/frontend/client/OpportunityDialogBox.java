package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpportunityDialogBox extends DialogBox implements ClickHandler {
	SubjectsSuggestWidget subWidget = new SubjectsSuggestWidget();
	MentorServiceAsync service;
	TextArea txtMessage;
	TextArea tbLocation = new TextArea();
	Button btnCreate, btnCancel, btnClear, btnPrev, btnNext;
	List<OpportunityVO> myOpps = new ArrayList<OpportunityVO>();
	LocationData lData = new LocationData();
	MapUI mapUI;
	int oppCount=0;
	StackPanel stackPanel = new StackPanel();

	public OpportunityDialogBox() {

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		setSize("400px", "608px");
		setHTML("Opportunity");

		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail to get Subjects list");
			}

			@Override
			public void onSuccess(List<String> result) {
				subWidget.addMoreSubjects(result);
			}
		};
		service.getSubjects(callback);
		
		AsyncCallback<List<OpportunityVO>> callback1 = new AsyncCallback<List<OpportunityVO>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get opportunity"+ caught.getMessage());
			}

			@Override
			public void onSuccess(List<OpportunityVO> result) {
				Window.alert(""+result.size());
				myOpps=result;
		//		if(result.size()!=0)
					myOpps();
			}

		};
		service.getOpportunitiesById("hm09j25q0n@kawanan.com", callback1);
		
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

		txtMessage = new TextArea();
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
		
		Label myOppLabel = new Label("My Opportunities:");
//		HorizontalPanel hp1 = new HorizontalPanel();
		btnPrev = new Button("My Opp");
//		btnNext = new Button("Next");
//		hp1.add(myOppLabel);
		hp.add(btnPrev);
		btnPrev.addClickHandler(this);
//		hp1.add(btnNext);
//		btnNext.addClickHandler(this);
//		verticalPanel.add(hp1);

		stackPanel.add(verticalPanel,"Set Opportunity");
		
		horizontalPanel.add(stackPanel);
		mapUI = new MapUI(false, tbLocation);
		mapUI.setWidth("500px");
		horizontalPanel.add(mapUI);
	}

	@Override
	public void onClick(ClickEvent event) {
		lData = mapUI.getLocationDetails();
		if (event.getSource() == btnCreate) {

			if (!(subWidget.selected.getSubjects().isEmpty())
					&& !(tbLocation.getText().contains("Please, Use the Map"))) {

				OpportunityVO oppVO = new OpportunityVO(null,
						subWidget.selected.getSubjects(), 0, 0,
						lData.getLatitude(), lData.getLongitude(), 0,
						tbLocation.getText());

				service.createOpportunity(oppVO,
						new AsyncCallback<OpportunityVO>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Sorry, Unble to Create the Opportunity"
										+ caught.getMessage());
							}

							@Override
							public void onSuccess(OpportunityVO result) {
								Window.alert("You Have successfully created an Opportunity");
								hideOpportunityDialogBox();
							}

						});
			}

		}

		if (event.getSource() == btnCancel) {
			subWidget.selected.clearAll();
			tbLocation.setText("Please, Use the Map");
			txtMessage.setText("");
			hideOpportunityDialogBox();
		}

		if (event.getSource() == btnClear) {
			subWidget.selected.clearAll();
			tbLocation.setText("Please, Use the Map");
			txtMessage.setText("");
		}
		
		if(event.getSource() == btnPrev){
			if(myOpps.get(oppCount)!= null){
				subWidget.selected.clearAll();
				txtMessage.setText("");
				tbLocation.setText(myOpps.get(oppCount).getLocString());
				mapUI.setMarkerLocation(myOpps.get(oppCount).getLatitude(), myOpps.get(oppCount).getLongitude());
				for(String sub: myOpps.get(oppCount).getSubjects())
					subWidget.selected.add(sub);
				oppCount++;
			}
		}
//		
//		if(event.getSource() == btnNext){
//			if(myOpps.get(oppCount)!= null){
//				subWidget.selected.clearAll();
//				txtMessage.setText("");
//				tbLocation.setText(myOpps.get(oppCount).getLocString());
//				mapUI.setMarkerLocation(myOpps.get(oppCount).getLatitude(), myOpps.get(oppCount).getLongitude());
//				for(String sub: myOpps.get(oppCount).getSubjects())
//					subWidget.selected.add(sub);
//				oppCount--;
//			}
//			
//		}

	}

	protected void hideOpportunityDialogBox() {
		this.hide();
	}
	
	
	private void myOpps(){
		if(myOpps.size() == 0){
			stackPanel.add(new Label("No opportunity is created by you.. "),"My Opportunity");
		}else {
			ScrollPanel scroller = new ScrollPanel();
			for(final OpportunityVO opp : myOpps){
				String subs=opp.getSubjects().get(0);
				int size=opp.getSubjects().size();				
				for(int i=1; i<size; i++ ){
					subs+=","+opp.getSubjects().get(i);
				}
				VerticalPanel vp = new VerticalPanel();
				Button edit = new Button("Edit");
				Label l = new Label("Subject: "+subs+"\n"+"Location: "+opp.getLocString()+"\n");
				l.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						mapUI.setMarkerLocation(opp.getLatitude(), opp.getLongitude());
					}
					
				});
				edit.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						subWidget.selected.clearAll();
						txtMessage.setText("");
						tbLocation.setText(myOpps.get(oppCount).getLocString());
						mapUI.setMarkerLocation(myOpps.get(oppCount).getLatitude(), myOpps.get(oppCount).getLongitude());
						for(String sub: myOpps.get(oppCount).getSubjects())
							subWidget.selected.add(sub);
						stackPanel.showStack(0);
					}
					
				});
				vp.add(l);
				vp.add(edit);
				scroller.add(vp);
			}
			stackPanel.add(scroller,"My Opportunity");
		}
	}
}