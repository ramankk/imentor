package com.bugyal.imentor.frontend.client;

import java.util.Arrays;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MockMentorServiceAsync implements MentorServiceAsync {

	// Set<ParticipantVO> created = new HashSet<ParticipantVO>();

	@Override
	public void create(ParticipantVO p, AsyncCallback<ParticipantVO> callback) {
		// created.add(p);
		callback.onSuccess(p);
	}

	public boolean isCreated(ParticipantVO p) {
		// return created.contains(p);
		return false;
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
	public void feedToMe(String emailId, AsyncCallback<SearchResponse> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createSession(String emailId, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateSession(String emailId, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSession(String emailId, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub

	}

	public void filterList(double latitude, double longitude,
			String strlocation, int radius, List<String> hasSubs,
			List<String> needSubs, AsyncCallback<SearchResponse> callback) {

	}

	public void localActivity(String email,
			AsyncCallback<SearchResponse> asyncCallback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateRandomData(int range, AsyncCallback<Void> asyncCallback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRecords(AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getOpportunitiesById(String emailId,
			AsyncCallback<List<OpportunityVO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createOpportunity(String emailId, OpportunityVO o,
			AsyncCallback<OpportunityVO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOpportunity(OpportunityVO o, String emailId,
			AsyncCallback<OpportunityVO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getParticipantVOByEmailId(String emailId,
			AsyncCallback<ParticipantVO> callback) {
		// TODO Auto-generated method stub

	}
}
