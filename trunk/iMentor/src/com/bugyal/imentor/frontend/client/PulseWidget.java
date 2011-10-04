package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.PulseVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;

public class PulseWidget extends Composite {
	MentorServiceAsync service;
	MapUI mapUI;
	List<PulseVO> pulseVOList;
	boolean runPulse=false;
	public PulseWidget(){
		service = (MentorServiceAsync) GWT.create(MentorService.class);
		service.getParticipantPulse(50, new AsyncCallback<List<PulseVO>>() {
			
			@Override
			public void onSuccess(List<PulseVO> arg0) {
				pulseVOList = arg0;
				if(arg0.size() != 0){
					runPulse = true;
				}
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("failed to run pulse "+arg0.getMessage());
			}
		});
		
		mapUI = new MapUI(false, null);
		
		TabPanel tabPanel = new TabPanel();
		tabPanel.add(mapUI, "IMentor Pulse");
		tabPanel.selectTab(0);
		
		initWidget(tabPanel);
	}
	
	public void runMapPulse(){
		if(runPulse){
			mapUI.mapPulse(pulseVOList);
		}
	}
}
