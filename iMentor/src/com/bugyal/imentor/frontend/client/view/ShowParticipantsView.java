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

public class ShowParticipantsView extends Composite {

	private static ShowParticipantsViewUiBinder uiBinder = GWT
			.create(ShowParticipantsViewUiBinder.class);

	interface ShowParticipantsViewUiBinder extends
			UiBinder<Widget, ShowParticipantsView> {
	}

	@UiField
	Button button;

	public ShowParticipantsView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

}
