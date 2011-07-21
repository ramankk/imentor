package com.bugyal.imentor.frontend.client;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.dev.util.collect.HashSet;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MockMentorServiceAsync implements MentorServiceAsync {

//	Set<ParticipantVO> created = new HashSet<ParticipantVO>();
	
	@Override
	public void create(ParticipantVO p, AsyncCallback<ParticipantVO> callback) {
	//	created.add(p);
		callback.onSuccess(p);
	}
	
	public boolean isCreated(ParticipantVO p) {
//		return created.contains(p);
		return false;
	}
	

	@Override
	public void createOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void find(List<String> subjects, ParticipantVO me,
			AsyncCallback<List<OpportunityVO>> callback) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void getSubjects(AsyncCallback<List<String>> callback) {
		String[] subs = { "abc", "def" };
		List<String> subjects = Arrays.asList(subs);
		callback.onSuccess(subjects);
	}

	@Override
	public void update(ParticipantVO p, AsyncCallback<ParticipantVO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback) {
		// TODO Auto-generated method stub

	}

}
