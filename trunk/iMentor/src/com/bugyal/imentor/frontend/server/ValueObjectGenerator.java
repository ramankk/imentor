package com.bugyal.imentor.frontend.server;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.PulseVO;
import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;
import com.bugyal.imentor.server.data.ParticipantPulse;

public class ValueObjectGenerator {

	public static ParticipantVO create(Participant p) {
		if (p == null) {
			return null;
		}
		return new ParticipantVO(p.getKey().getId(), p.getName(),
				p.getGender(), p.getEmail(),p.getFacebookId(), p.getLoc().getLatitude(), p
						.getLoc().getLongitude(), p.getLoc()
						.getLocationString(), p.getLoc().getActiveRadius(),
				p.getHasSubjects(), p.getNeedSubjects());
	}

	public static List<PulseVO> createPulseVO(List<ParticipantPulse> pulseList) {
		List<PulseVO> pulseRecords = new ArrayList<PulseVO>();
		for(ParticipantPulse p : pulseList) {
			PulseVO pv=new PulseVO(p.getEmailId(), p.getName(), p.getFacebookId(), p.getLongitude(), p.getLatitude(), p.getLocationString(), p.isMentor());
			pulseRecords.add(pv);
		}
		return pulseRecords;		
	}
	
	public static OpportunityVO create(Opportunity o) {
		// TODO(raman): Understand if participant info has to be supplied.
		// TODO(sudhakar): Add support for saying active vs. passive
		// opportunity.
		if (o == null) {
			return null;
		}
		
		return new OpportunityVO(o.getKey().getId(), o.getSubjects(),
				o.getRequiredMentors(), o.getPriority(), o.getLoc()
						.getLatitude(), o.getLoc().getLongitude(), o.getLoc()
						.getActiveRadius(), o.getLoc().getLocationString(),
				o.getMessage(),o.getLastModifiedTime());
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
		if (opList != null) {
			for (Opportunity p : opList) {
				opVos.add(ValueObjectGenerator.create(p));
			}
		}
		return opVos;
	}	
}
