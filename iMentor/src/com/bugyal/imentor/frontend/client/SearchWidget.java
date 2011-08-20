package com.bugyal.imentor.frontend.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchWidget extends Composite implements ClickHandler, ChangeHandler{

	 

	public SearchWidget() {
		
		VerticalPanel mainPanel = new VerticalPanel();
		initWidget(mainPanel);
		mainPanel.setSize("750px", "756px");
	
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		mainPanel.add(horizontalPanel);
		horizontalPanel.setSize("750px", "317px");
		
		VerticalPanel filterPanel = new VerticalPanel();
		horizontalPanel.add(filterPanel);
		filterPanel.setSize("255px", "315px");
		
		Label label = new Label("Select Subjects :");
		filterPanel.add(label);
		
		SubjectsSuggestWidget subjectsSuggestWidget = new SubjectsSuggestWidget();
		subjectsSuggestWidget.suggestBox.setWidth("243px");
		filterPanel.add(subjectsSuggestWidget);
		subjectsSuggestWidget.setSize("250px", "75px");
		
		Label label_1 = new Label("Select Location:");
		filterPanel.add(label_1);
		
		TextArea location = new TextArea();
		location.setText("Location");
		filterPanel.add(location);
		location.setSize("243px", "75px");
		
		Button button = new Button("Search");
		filterPanel.add(button);
		filterPanel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_RIGHT);
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		horizontalPanel.add(verticalPanel_2);
		verticalPanel_2.setSize("483px", "314px");
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel_2.add(horizontalPanel_3);
		horizontalPanel_3.setSize("491px", "33px");
		
		Label lblNewLabel = new Label("Search By:");
		horizontalPanel_3.add(lblNewLabel);
		horizontalPanel_3.setCellVerticalAlignment(lblNewLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_3.setCellHorizontalAlignment(lblNewLabel, HasHorizontalAlignment.ALIGN_CENTER);
		
		RadioButton rdbtnNewRadioButton = new RadioButton("new name", "All");
		horizontalPanel_3.add(rdbtnNewRadioButton);
		horizontalPanel_3.setCellVerticalAlignment(rdbtnNewRadioButton, HasVerticalAlignment.ALIGN_MIDDLE);
		
		RadioButton rdbtnNewRadioButton_1 = new RadioButton("new name", "Mentor");
		horizontalPanel_3.add(rdbtnNewRadioButton_1);
		horizontalPanel_3.setCellVerticalAlignment(rdbtnNewRadioButton_1, HasVerticalAlignment.ALIGN_MIDDLE);
		
		RadioButton rdbtnNewRadioButton_2 = new RadioButton("new name", "Mentee");
		horizontalPanel_3.add(rdbtnNewRadioButton_2);
		horizontalPanel_3.setCellVerticalAlignment(rdbtnNewRadioButton_2, HasVerticalAlignment.ALIGN_MIDDLE);
		
		FlexTable flexTable = new FlexTable();
		verticalPanel_2.add(flexTable);
		verticalPanel_2.setCellVerticalAlignment(flexTable, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.setSize("490px", "242px");
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		verticalPanel_2.add(horizontalPanel_2);
		verticalPanel_2.setCellHorizontalAlignment(horizontalPanel_2, HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel_2.setSize("57px", "29px");
		
		Button prevBtn = new Button("<");
		horizontalPanel_2.add(prevBtn);
		
		Button nextBtn = new Button(">");
		horizontalPanel_2.add(nextBtn);
		
		MapUI mapUI = new MapUI(true, location);
		mapUI.setSize("750px", "419px");
		mainPanel.add(mapUI);
	}

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
