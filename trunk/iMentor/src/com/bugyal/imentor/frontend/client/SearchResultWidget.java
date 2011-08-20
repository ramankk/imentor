package com.bugyal.imentor.frontend.client;

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

		hp.add(name);
		hp.add(subjects);
		hp.add(rel);

		initWidget(hp);
	}

	public void setResult(SearchResult result) {
		if (result.isTypeParticipant()) {
			name.setText(result.getP().getName());

			if (result.isHas()) {
				rel.setText(" can help you in ");
			} else {
				rel.setText(" is looking for your help in ");
			}
		} else {
			name.setText("Opportunity in ");
			rel.setText("at "+result.getO().getLocString());
		}
		String subs = " ";
		for (String str : result.getSubjects()) {
			subs += str + ", ";
		}
		subjects.setText(subs.substring(0, subs.length() - 2));
	}

	public void clear() {
		name.setText("");
		rel.setText("");
		subjects.setText("");
	}

}
