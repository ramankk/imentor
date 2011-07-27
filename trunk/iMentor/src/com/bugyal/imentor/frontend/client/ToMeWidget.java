package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.cellview.client.CellTable;

public class ToMeWidget extends Composite {

	FlexTable toMeList;
	public ToMeWidget() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("597px", "235px");
		
		Label lblToMe = new Label("To Me");
		lblToMe.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(lblToMe);
		lblToMe.setHeight("25px");
		
		
		addDataToCellTable();
		verticalPanel.add(lblToMe);
		
		CellTable<CellTableObject> cellTable = new CellTable<CellTableObject>();
		verticalPanel.add(cellTable);
		cellTable.setSize("596px", "178px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		Button button = new Button("<");
		horizontalPanel.add(button);
		
		Button button_1 = new Button(">");
		horizontalPanel.add(button_1);
	}
	private void addDataToCellTable() {
		for(int i=0;i<5;i++)
		{
			
		}
		
	}

}
