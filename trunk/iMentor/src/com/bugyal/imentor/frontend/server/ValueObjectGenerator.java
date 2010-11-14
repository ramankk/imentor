package com.bugyal.imentor.frontend.server;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.server.data.Opportunity;
import com.bugyal.imentor.server.data.Participant;

public class ValueObjectGenerator {

	public static ParticipantVO create(Participant p) {
		// TODO(raman): Dont send all of datastore key.
		// TODO(raman): also add need-subjects.
		return new ParticipantVO(p.getKey().getId(), p.getName(), p.getEmail(), p
				.getLoc().getLatitude(), p.getLoc().getLongitude(), p.getLoc()
				.getLocationString(), p.getLoc().getActiveRadius(), p
				.getHasSubjects(), p.getNeedSubjects());
	}

	public static OpportunityVO create(Opportunity o) {
		// TODO(raman): Dont send all of datastore key.
		// TODO(raman): Understand if participant info has to be supplied.

		return new OpportunityVO(o.getKey().getId(), o.getSubjects(), o
				.getRequiredMentors(), o.getPriority(), o.getLoc().getLatitude(),
				o.getLoc().getLongitude(), o.getLoc().getActiveRadius(), o.getLoc().getLocationString());
	}
}
