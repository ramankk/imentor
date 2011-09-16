package com.bugyal.imentor.frontend.client;

import java.util.Date;
import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.ibm.icu.text.DateFormat;

public class MyOpportunitiesWidget extends Composite {
	OpportunityPanel parent;
	FlexTable scroller;
	List<OpportunityVO> oppList;
	HTML edit, remove;

	public MyOpportunitiesWidget(OpportunityPanel opportunityDialogBox) {
		parent = opportunityDialogBox;
		scroller = new FlexTable();

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(scroller);
		initWidget(hp);

		scroller.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final int cellIndex = scroller.getCellForEvent(event)
						.getCellIndex();
				final int rowIndex = scroller.getCellForEvent(event)
						.getRowIndex();

				if (cellIndex == 1) {
					parent.showOpportunity(oppList.get(rowIndex));
				} else if (cellIndex == 2) {
					parent.deleteOpportunityById(oppList.get(rowIndex).getId());
					oppList.remove(rowIndex);
					setOpportunities(oppList);
				}
			}// onClick
		});

	}

	public void setOpportunities(List<OpportunityVO> myOppsList) {
		oppList = myOppsList;
		scroller.clear();
		for (int i = 0; i < myOppsList.size(); i++) {

			edit = new HTML("<a>Edit</a>");
			remove = new HTML("<a>X</a>");

			edit.setTitle("click to edit the Opportunity");
			edit.setStyleName("removeSubjectCSS");
			remove.setTitle("Click to delete the Opportunity");
			remove.setStyleName("removeSubjectCSS");

			scroller.setWidget(i, 0, getOpportunityVP(oppList.get(i), parent));
			scroller.setWidget(i, 1, edit);
			scroller.setWidget(i, 2, remove);
		}
	}

	private HorizontalPanel getOpportunityVP(final OpportunityVO o,
			final OpportunityPanel parent) {
		Date d = new Date(o.getLastModifiedTime());

		Label date = new Label(DateTimeFormat.getMediumDateFormat().format(d));
		Label subjects = new Label(o.getSubjectsAsString());
		Label location = new Label(o.getLocString());

		location.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				parent.showOnMap(o);
			}
		});

		HorizontalPanel ovp = new HorizontalPanel();
		ovp.add(date);
		ovp.add(new HTML("<b>&nbsp</b>"));
		ovp.add(new Label("on"));
		ovp.add(new HTML("<b>&nbsp</b>"));
		ovp.add(subjects);
		ovp.add(new HTML("<b>&nbsp</b>"));
		ovp.add(new Label("at"));
		ovp.add(new HTML("<b>&nbsp</b>"));
		ovp.add(location);

		return ovp;

	}
}
