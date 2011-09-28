package com.bugyal.imentor.frontend.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MentorDataStatus implements IsSerializable {
	private List<MentorsResult> mentorslist;
	private boolean isExisted;
	
	public MentorDataStatus() {		
	}
	
	public MentorDataStatus(List<MentorsResult> mentorslist, boolean isExisted) {
		this.mentorslist = mentorslist;
		this.isExisted = isExisted;
	}
	
	public List<MentorsResult> getMentorslist() {
		return mentorslist;
	}
	public void setMentorslist(List<MentorsResult> mentorslist) {
		this.mentorslist = mentorslist;
	}
	public boolean isExisted() {
		return isExisted;
	}
	public void setExisted(boolean isExisted) {
		this.isExisted = isExisted;
	}
}
