package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class SubjectsSuggestWidget extends Composite {

	final MultiWordSuggestOracle oracle;
	public final SuggestBox suggestBox;

	private final FlowPanel flowpanel;
	private List<String> list;
	private boolean status;

	@SuppressWarnings("unchecked")
	public SubjectsSuggestWidget(List<String> subjects) {

		oracle = new MultiWordSuggestOracle();
		suggestBox = new SuggestBox(oracle);
		suggestBox.setWidth("15px");
		DOM.setStyleAttribute(suggestBox.getElement(), "border", "0");
		DOM.setStyleAttribute(suggestBox.getElement(), "outline", "0");
		this.oracle.addAll(subjects);

		flowpanel = new FlowPanel();
		flowpanel.setTitle("Type and choose related Subjects");
		flowpanel.addStyleName("flowPanelCSS");

		flowpanel.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				suggestBox.setFocus(true);
			}

		}, ClickEvent.getType());

		flowpanel.add(suggestBox);

		list = new ArrayList<String>();

		suggestBox.addSelectionHandler(new SelectionHandler() {
			@Override
			public void onSelection(SelectionEvent event) {
				String str = suggestBox.getText();
				if (add(str)) {
					suggestBox.setText("");
				}
			}
		});
		initWidget(flowpanel);
	}

	public boolean add(String item) {
		HTML html = new HTML("<a>x</a>");
		html.setStyleName("removeSubjectCSS");

		status = false;
		for (String s : list) {
			if (s.equalsIgnoreCase(item)) {
				status = true;
				break;
			}
		}
		if (!status) {
			list.add(item);
			final FlexTable flextable = new FlexTable();
			flextable.addStyleName("itemSubjectCSS");

			flextable.setWidget(0, 0, new Label(item));
			flextable.setWidget(0, 1, html);

			DOM.setStyleAttribute(flextable.getElement(), "border",
					"1px solid #00f");
			flowpanel.insert(flextable, flowpanel.getWidgetCount() - 1);
			status = true;

			flextable.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final int cellIndex = flextable.getCellForEvent(event)
							.getCellIndex();
					if (cellIndex == 1) {
						String temp = flextable.getText(0, 0);
						flextable.removeFromParent();
						remove(temp);
					}
				}// onClick
			});
		}

		return status;
	}

	public boolean remove(String item) {
		return list.remove(item);
	}

	public List<String> getSubjects() {
		return list;
	}

	public void clearAll() {
		list.clear();
		flowpanel.clear();
		
		flowpanel.add(suggestBox);
	}

	public void addMoreSubjects(String subject) {
		this.oracle.add(subject);
	}

	public void addMoreSubjects(Collection<String> subjects) {
		this.oracle.addAll(subjects);
	}

	public List<String> getSelected() {
		return this.getSubjects();
	}

}