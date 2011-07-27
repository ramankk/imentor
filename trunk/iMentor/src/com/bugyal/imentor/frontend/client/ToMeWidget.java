package com.bugyal.imentor.frontend.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class ToMeWidget extends Composite {
	
	FlexTable toMeList;
	VerticalPanel verticalPanel = new VerticalPanel();
	VerticalPanel contentPanel = new VerticalPanel();
	Button forwardButton = new Button(">");
	Button backwardButton = new Button("<");
	static int start=0;
	static int end=0;
	
	public ToMeWidget() {
				
		initWidget(verticalPanel);
		verticalPanel.setSize("597px", "235px");
		
		Label lblToMe = new Label("To Me");
		lblToMe.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(lblToMe);
		lblToMe.setHeight("25px");		
		
		contentPanel.setSize("600px", "180px");
		verticalPanel.add(contentPanel);
		DOM.setStyleAttribute(contentPanel.getElement(), "border", "1px solid #00f");
		end = start+5;
		addDataFeeds(start, end);
				
//		CellTable<CellTableObject> cellTable = new CellTable<CellTableObject>();
//		verticalPanel.add(cellTable);
//		cellTable.setSize("596px", "178px");
//		cellTable.setVisible(true);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		horizontalPanel.add(backwardButton);
		backwardButton.setEnabled(false);
		
		horizontalPanel.add(forwardButton);
				
		backwardButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(start > 0){
					contentPanel.clear();
					end = start;
					addDataFeeds(start-=5, end);
				}
			}			
		});
		
		forwardButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				if(end < 100){
					contentPanel.clear();
					start = end;
					addDataFeeds(start, end += 5);
				}				
			}			
		});
	}
	
	
	private void addDataFeeds(int start, int end) {		
		for(int i=start; i<end; i++){
			Label nameFeed = new Label(i+" Ravi");
			Label dataFeed = new Label("Looks for ");
			Label subjectsFeed = new Label("Maths, Science");
			HTML closeButton = new HTML("<a>x</a>");

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(nameFeed);
			hp.add(dataFeed);
			hp.add(subjectsFeed);
			hp.add(closeButton);
			
			contentPanel.add(hp);
		}
		
		if(start != 0)
			backwardButton.setEnabled(true);
		else
			backwardButton.setEnabled(false);
		
		if(end == 100)
			forwardButton.setEnabled(false);
		else
			forwardButton.setEnabled(true);
	}
}
