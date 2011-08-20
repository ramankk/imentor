package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MentorServiceAsync {

	void find(List<String> subjects, ParticipantVO me,
			AsyncCallback<List<OpportunityVO>> callback);

	void create(ParticipantVO p, AsyncCallback<ParticipantVO> callback);

	void update(ParticipantVO p, AsyncCallback<ParticipantVO> callback);

	void updateOpportunity(OpportunityVO o, String emailId,
			AsyncCallback<OpportunityVO> callback);
	
	void getSubjects(AsyncCallback<List<String>> callback);

	void feedToMe(String emailId, AsyncCallback<SearchResponse> callback);

	void filterList(double latitude, double longitude, String strlocation,
			int radius, List<String> hasSubs, List<String> needSubs,
			AsyncCallback<SearchResponse> callback);

	void localActivity(String email, AsyncCallback<SearchResponse> asyncCallback);

	void generateRandomData(int range, AsyncCallback<Void> asyncCallback);

	void deleteRecords(AsyncCallback<Long> asyncCallback);

	void getOpportunitiesById(String emailId,
			AsyncCallback<List<OpportunityVO>> callback);

	void createOpportunity(String emailId, OpportunityVO o,
			AsyncCallback<OpportunityVO> callback);	

}
