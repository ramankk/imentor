package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;

public class ValueObjectGenerator {

	public static ParticipantVO create(Participant p) {
		// TODO(raman): Dont send all of datastore key.
		// TODO(raman): also add need-subjects.
		return new ParticipantVO(p.getKey().getId(), p.getName(), p.getGender(), p.getEmail(), p
				.getLoc().getLatitude(), p.getLoc().getLongitude(), p.getLoc()
				.getLocationString(), p.getLoc().getActiveRadius(), p
				.getHasSubjects(), p.getNeedSubjects());
	}

	public static OpportunityVO create(Opportunity o) {
		// TODO(raman): Dont send all of datastore key.
		// TODO(raman): Understand if participant info has to be supplied.
		// TODO(sudhakar): Add support for saying active vs. passive opportunity.
		return new OpportunityVO(o.getKey().getId(), o.getSubjects(), o
				.getRequiredMentors(), o.getPriority(), o.getLoc().getLatitude(),
				o.getLoc().getLongitude(), o.getLoc().getActiveRadius(), o.getLoc().getLocationString(), o.getMessage());
	}
	
	public static List<ParticipantVO> createParticipantVOs(
			List<Participant> participantsList) {
		List<ParticipantVO> participantVOList = new ArrayList<ParticipantVO>();
		for (Participant p : participantsList) {
			participantVOList.add(ValueObjectGenerator.create(p));
		}
		return participantVOList;
	}
	
	public static List<OpportunityVO> createOpportunityVOs(
			List<Opportunity> opList) {
		List<OpportunityVO> opVos = new ArrayList<OpportunityVO>();
		for (Opportunity p : opList) {
			opVos.add(ValueObjectGenerator.create(p));
		}
		return opVos;
	}
}
