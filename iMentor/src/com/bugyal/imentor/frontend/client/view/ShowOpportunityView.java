package com.bugyal.imentor.frontend.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ShowOpportunityView extends Composite {

	private static ShowOpportunityViewUiBinder uiBinder = GWT
			.create(ShowOpportunityViewUiBinder.class);

	interface ShowOpportunityViewUiBinder extends
			UiBinder<Widget, ShowOpportunityView> {
	}

	@UiField
	Button button;

	public ShowOpportunityView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

}
