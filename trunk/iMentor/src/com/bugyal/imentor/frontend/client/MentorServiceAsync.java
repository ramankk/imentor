package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.MentorsResult;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MentorServiceAsync {

	void find(List<String> subjects, ParticipantVO me,
			AsyncCallback<List<OpportunityVO>> callback);

	void create(ParticipantVO p, AsyncCallback<ParticipantVO> callback);

	void update(ParticipantVO p, AsyncCallback<ParticipantVO> callback);

	void updateOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback);

	void getSubjects(AsyncCallback<List<String>> callback);

	void feedToMe(AsyncCallback<SearchResponse> callback);

	void filterList(double latitude, double longitude, String strlocation,
			int radius, List<String> hasSubs, List<String> needSubs,
			AsyncCallback<SearchResponse> callback);


	void getParticipantVOByEmailId(AsyncCallback<ParticipantVO> callback);

	void createSession(String emailId, String provider, String providerId,
			AsyncCallback<Boolean> callback);

	void deleteSession(AsyncCallback<Boolean> callback);

	void localActivity(AsyncCallback<SearchResponse> asyncCallback);

	void generateRandomData(int range, AsyncCallback<Void> asyncCallback);

	void deleteRecords(AsyncCallback<Long> asyncCallback);

	void getOpportunitiesById(AsyncCallback<List<OpportunityVO>> callback);

	void createOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback);

	void addMentorAndMentee(Boolean isHas, String mentorMailId, AsyncCallback<Boolean> callback);

	void commment(String subject, String comment,
			AsyncCallback<Void> callback);	
	

	void getMentorAndMentees(ParticipantVO participantVO,
			AsyncCallback<List<MentorsResult>> callback1);	
}
