package com.bugyal.imentor.frontend.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MentorsResult implements IsSerializable {
	private String name;
	private boolean isMentor;
	
	public MentorsResult(){
		
	}
	
	public MentorsResult(String name, boolean isMentor) {
		super();
		this.name = name;
		this.isMentor = isMentor;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setMentor(boolean isMentor) {
		this.isMentor = isMentor;
	}
	public boolean isMentor() {
		return isMentor;
	}
}
