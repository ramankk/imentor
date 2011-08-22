package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.MeException;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResponse;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("mentor")
public interface MentorService extends RemoteService {

	ParticipantVO create(ParticipantVO p) throws MeException;

	OpportunityVO createOpportunity(OpportunityVO o) throws MeException;

	ParticipantVO update(ParticipantVO p) throws MeException;

	OpportunityVO updateOpportunity(OpportunityVO o) throws MeException;

	SearchResponse feedToMe() throws MeException;

	List<OpportunityVO> find(List<String> subjects, ParticipantVO me)
			throws MeException;

	List<String> getSubjects() throws MeException;

	SearchResponse filterList(double latitude, double longitude,
			String strlocation, int radius, List<String> hasSubs,
			List<String> needSubs) throws MeException;

	ParticipantVO getParticipantVOByEmailId() throws MeException;

	void createSession(String emailId) throws MeException;

	boolean deleteSession() throws MeException;

	SearchResponse localActivity() throws MeException;

	void generateRandomData(int range) throws MeException;

	long deleteRecords() throws MeException;

	List<OpportunityVO> getOpportunitiesById() throws MeException;
}
