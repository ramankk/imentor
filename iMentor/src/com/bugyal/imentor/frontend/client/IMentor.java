package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class IMentor implements EntryPoint {

	TextBox tb;
	ToMeWidget tw;

	public void onModuleLoad() {
		RootPanel.get("head").add(new HeaderWidget());
		tb = new TextBox();
		Button button = new Button("set user");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(tb);
		hp.add(button);

		RootPanel.get().add(hp);

		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (tw == null) {
					tw = new ToMeWidget(tb.getText());
					RootPanel.get("tome").add(tw);
				} else {
					tw.getDataFeeds(tb.getText());
				}
				
			}
		});
	}
}