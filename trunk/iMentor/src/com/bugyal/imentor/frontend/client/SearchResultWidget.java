package com.bugyal.imentor.frontend.client;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class SearchResultWidget extends Composite {

	String[] colors = { "#FFB90F", "#DAA520" };

	Label name = new Label();
	Label rel = new Label();
	Label subjects = new Label();

	HorizontalPanel hp = new HorizontalPanel();

	public SearchResultWidget(boolean isEven) {
		hp.setSize("600px", "30px");

		DOM.setStyleAttribute(hp.getElement(), "backgroundColor",
				isEven ? colors[0] : colors[1]);

		// Refer StyleSheet named "war/ToMeWidget.css"
		name.addStyleName("myLabelCSS");
		rel.addStyleName("myLabelCSS");
		subjects.addStyleName("myLabelCSS");

		hp.add(name);
		hp.add(rel);
		hp.add(subjects);

		initWidget(hp);
	}

	public void setResult(SearchResult result) {
		if (result.isTypeParticipant()) {
			name.setText(result.getP().getName());

			if (result.isHas()) {
				// TODO(Sridhar): Confirm the text before submitting..
				rel.setText(" is looking for your help in ");
			} else {
				rel.setText(" can help you in ");
			}
		} else {
			// TODO(Sridhar): Fix my format before submitting..
			name.setText("Opportunity at " + result.getO().getLocString());
			rel.setText(", looking for your help in ");
		}
		String subs = " ";
		for (String str : result.getSubjects()) {
			subs += str + ", ";
		}
		subjects.setText(subs);
	}

}
