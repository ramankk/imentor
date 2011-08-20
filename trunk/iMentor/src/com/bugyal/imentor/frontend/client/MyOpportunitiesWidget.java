package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyOpportunitiesWidget extends Composite {
	OpportunityDialogBox parent;
	VerticalPanel scroller= new VerticalPanel();
	
	public MyOpportunitiesWidget(OpportunityDialogBox opportunityDialogBox) {
		Window.alert("My OPPs class");
		parent = opportunityDialogBox;
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(scroller);
		initWidget(hp);
	}
	
	public void setOpportunities(List<OpportunityVO> myOppsList) {
		scroller.clear();
		for (OpportunityVO opp : myOppsList) {
			scroller.add(new SingleOpportunityResult(opp, parent));
		}
	}

	public static class SingleOpportunityResult extends Composite {
		VerticalPanel vp = new VerticalPanel();

		public SingleOpportunityResult(final OpportunityVO o,
				final OpportunityDialogBox parent) {
			Window.alert("My OPPs Inner class");
			HorizontalPanel panel = new HorizontalPanel();
			Label lhead = new Label("Created On : "); // +opp.getLastModifiedTime());
			Button edit = new Button("Edit");
			edit.setHeight("20px");
			panel.add(lhead);
			panel.add(edit);
			Label l = new Label("Subject:" + o.getSubjectsAsString()
					+ " Location:" + o.getLocString() + " ");
			DOM.setStyleAttribute(l.getElement(), "border", "1px solid black");

			l.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					parent.showOnMap(o);
				}
			});
			edit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					parent.showOpportunity(o);
				}
			});
			vp.add(panel);
			vp.add(l);
			initWidget(vp);

			Window.alert("My OPPs Inner class completed");
		}
	}
}
