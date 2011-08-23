package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

public class SearchResultWidget extends Composite {

	String[] colors = {"#F0FAFA", "#FFFFFF"};

	Label message = new Label();
	Label distance = new Label();
	Anchor pursueLink = new Anchor("->");
	
	FlexTable table = new FlexTable();

	public SearchResultWidget(boolean isEven) {
		table.setSize("730px", "34px");

		DOM.setStyleAttribute(table.getElement(), "backgroundColor",
				isEven ? colors[0] : colors[1]);

		table.setWidget(0, 0, message);
		table.getFlexCellFormatter().setColSpan(0, 0, 5);
		table.setWidget(0, 6, distance);
		table.setWidget(0, 7, pursueLink);

		initWidget(table);
	}

	public void setResult(SearchResult result) {
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
		messageString.append("] ");
		message.setText(messageString.toString());
		
		distance.setText(result.getDistance());
	}

	public void clear() {
		message.setText("");
		distance.setText("");
		pursueLink.setHref("#");
	}
}
