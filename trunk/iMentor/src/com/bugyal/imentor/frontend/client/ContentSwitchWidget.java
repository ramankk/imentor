package com.bugyal.imentor.frontend.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class ContentSwitchWidget extends Composite {

	TabPanel tabPanel;
	//ToMeWidget tome ;
	
	public ContentSwitchWidget() {
				 	
		FlowPanel flowpanel;
		
		tabPanel = new TabPanel();

		flowpanel = new FlowPanel();
		flowpanel.add(new ToMeWidget("m9u62695zh@kawanan.com"));
		tabPanel.add(flowpanel, "All");

		flowpanel = new FlowPanel();
		flowpanel.add(new ToMeWidget("m9u62695zh@kawanan.com")); 
		tabPanel.add(flowpanel, "Need");

		flowpanel = new FlowPanel();
		flowpanel.add(new ToMeWidget("m9u62695zh@kawanan.com")); 
		tabPanel.add(flowpanel, "Has");

		tabPanel.selectTab(1);

		tabPanel.setSize("500px", "250px");
		tabPanel.addStyleName("table-center");
		
		initWidget(tabPanel);
		
	}

}


