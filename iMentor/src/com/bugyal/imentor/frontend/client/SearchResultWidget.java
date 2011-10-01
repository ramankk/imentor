package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class SearchResultWidget extends Composite implements SearchResultFactory.SearchResultWidgetInterface {

	String[] colors = { "#F0FAFA", "#FFFFFF" };

	Label message = new Label();
	Label distance = new Label();

	Image pursueImage = new Image();
	SearchResult searchResult = null;
	FlexTable table = new FlexTable();
	

	public SearchResultWidget(boolean isEven) {
		setEvenRow(isEven);
	}
	
	@Override
	public void setEvenRow(boolean isEven) {

		table.setSize("700px", "34px");
	
		DOM.setStyleAttribute(table.getElement(), "backgroundColor",
				isEven ? colors[0] : colors[1]);

		table.setWidget(0, 0, message);
		table.getFlexCellFormatter().setColSpan(0, 0, 5);
		table.setWidget(0, 6, distance);
		table.setWidget(0, 7, pursueImage);
		pursueImage.setSize("15px","15px");
		pursueImage.setUrl("images/magni.png");
		pursueImage.setVisible(false);
		table.getCellFormatter().setWidth(0, 6, "50px");
		table.getCellFormatter().setWidth(0, 7, "15px");
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
		StringBuilder messageString = new StringBuilder();
		if (result.isTypeParticipant()) {
			table.setTitle(result.getP().getLocationString());
			messageString.append(result.getP().getName());
			if (result.isHas()) {
				messageString.append(" can help you in ");
			} else {
				messageString.append(" is looking for your help in ");
			}
		} else {
			table.setTitle(result.getO().getLocString());
			messageString.append("Opportunity in ");
		}
		messageString.append(" [");
		for (String str : result.getSubjects()) {
			messageString.append(str).append(", ");
		}
		messageString.deleteCharAt(messageString.length()-1);
		messageString.deleteCharAt(messageString.length()-1);
		messageString.append("] ");
		message.setText(messageString.toString());

		distance.setText(result.getDistanceString());
	}
	
	@Override
	public void clear() {
		message.setText("");
		distance.setText("");
		pursueImage.setVisible(false);
	}	
}
