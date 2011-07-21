package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MentorServiceAsync {

	void find(List<String> subjects, ParticipantVO me,
			AsyncCallback<List<OpportunityVO>> callback);

	void create(ParticipantVO p, AsyncCallback<ParticipantVO> callback);

	void createOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback);

	void update(ParticipantVO p, AsyncCallback<ParticipantVO> callback);

	void updateOpportunity(OpportunityVO o,
			AsyncCallback<OpportunityVO> callback);
	
	void getSubjects(AsyncCallback<List<String>> callback);

}
