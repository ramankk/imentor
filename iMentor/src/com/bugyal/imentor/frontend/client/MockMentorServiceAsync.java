package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.MentorsResult;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MockMentorServiceAsync implements MentorServiceAsync {

	@Override
	public void create(ParticipantVO p, AsyncCallback<ParticipantVO> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRecords(AsyncCallback<Long> asyncCallback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSession(AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void feedToMe(AsyncCallback<SearchResponse> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filterList(double latitude, double longitude,
			String strlocation, int radius, List<String> hasSubs,
			List<String> needSubs, AsyncCallback<SearchResponse> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void find(List<String> subjects, ParticipantVO me,
			AsyncCallback<List<OpportunityVO>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateRandomData(int range, AsyncCallback<Void> asyncCallback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOpportunitiesById(AsyncCallback<List<OpportunityVO>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getParticipantVOByEmailId(AsyncCallback<ParticipantVO> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSubjects(AsyncCallback<List<String>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void localActivity(AsyncCallback<SearchResponse> asyncCallback) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void createSession(String emailId, String provider,
			String providerId, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMentorAndMentee(Boolean isHas, String mentorMailId,
			AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commment(String subject, String comment,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMentorAndMentees(ParticipantVO participantVO,
			AsyncCallback<List<MentorsResult>> callback1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteOpportunity(long id, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMentorToOpportunity(long id, AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

}
