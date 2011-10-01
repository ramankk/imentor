package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.client.SearchResultFactory.SearchResultWidgetInterface;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class SearchResultWidgetForProfile extends Composite implements
		SearchResultWidgetInterface {
	
	String[] colors = { "#F0FAFA", "#FFFFFF" };
	Label name = new Label();
	Label location = new Label();
	Label subjects = new Label();
	Image pursueImage = new Image();
	
	SearchResult searchResult = null;
	FlexTable table = new FlexTable();
	MapUI mapUI = null; 

	public SearchResultWidgetForProfile(boolean isEven, final MapUI mapUI) {
		setEvenRow(isEven);
		this.mapUI = mapUI;
		
		table.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (mapUI != null && searchResult != null) {
				  mapUI.showResult(searchResult);
				}
			}});
	}
	
	
	
	@Override
	public void setEvenRow(boolean isEven) {
		table.setSize("700px", "34px");	
		DOM.setStyleAttribute(table.getElement(), "backgroundColor",
				isEven ? colors[0] : colors[1]);
		table.setWidget(0, 0, name);
		//table.getCellFormatter().setWidth(0, 3, "130px");
		table.setWidget(0, 1, location);
		table.getCellFormatter().setWidth(0, 1, "400px");
		table.setWidget(0, 2, subjects);
		table.getCellFormatter().setWidth(0, 2, "300px");
		table.setWidget(0, 3, pursueImage);
		pursueImage.setSize("15px","15px");
		pursueImage.setUrl("images/magni.png");
		pursueImage.setVisible(false);
		table.getCellFormatter().setWidth(0, 3, "20px");
		
		DOM.setStyleAttribute(pursueImage.getElement(), "cursor", "pointer");
		pursueImage.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(searchResult == null) {
					Window.alert("Something Wrong with ur Action");
				}
				if(searchResult.isTypeParticipant()){					
					ProfileInfo info = new ProfileInfo(searchResult);
					info.center();					
				}
				else{
					OpportunityInfo info = new OpportunityInfo(searchResult);
					info.center();
				}
			}
		});		
		initWidget(table);		
	}

	@Override
	public void setResult(SearchResult result) {
		
		searchResult = result;
		
		pursueImage.setVisible(true);
		if(result.isTypeParticipant()) {
			name.setText(result.getP().getName());
			location.setText(result.getP().getLocationString());
		} else {
			name.setText("Opportunity at");
			location.setText(result.getO().getLocString());
		}
		StringBuilder subject = new StringBuilder();
		boolean first = true;
		for(String str: result.getSubjects()) {
			if (first) {
				first = false;
			} else {
				subject.append(", ");
			}
			subject.append(str);
		}
		subjects.setText(subject.toString());	
	}
	@Override
	public void clear() {
		name.setText("");
		location.setText("");
		subjects.setText("");
		pursueImage.setVisible(false);
	}

}
