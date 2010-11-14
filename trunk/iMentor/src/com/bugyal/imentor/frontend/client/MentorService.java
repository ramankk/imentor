package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.MeException;
import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
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
	
	
	List<OpportunityVO> find(List<String> subjects, ParticipantVO me) throws MeException;
}
