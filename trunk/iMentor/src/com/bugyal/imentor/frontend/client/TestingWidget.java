package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestingWidget extends Composite {
	TextBox tb, tb1;
	MentorServiceAsync service;
	HeaderWidget headerWidget;

	public TestingWidget(HeaderWidget header) {
		this.headerWidget = header;
		tb = new TextBox();
		Button button = new Button("set user");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(tb);
		hp.add(button);

		tb1 = new TextBox();
		Button btn = new Button("Generate data");
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(tb1);
		hp1.add(btn);

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				headerWidget.setEmailForTest(tb.getText());
				headerWidget.getMainPage().showHomeWidget();
			}
		});

		btn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int range = Integer.parseInt(tb1.getText());
				service = (MentorServiceAsync) GWT.create(MentorService.class);
				service.generateRandomData(range, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Exception : " + caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Data generation complete ");
					}
				});
			}
		});

		Button b = new Button("Delete Datastore Records");
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(b);
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service = (MentorServiceAsync) GWT.create(MentorService.class);
				service.deleteRecords(new AsyncCallback<Long>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to delete records"
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(Long result) {
						Window.alert("No of records deleted : " + result);
					}

				});
			}

		});

		VerticalPanel vp = new VerticalPanel();
		vp.add(hp);
		vp.add(hp1);
		vp.add(hp2);
		initWidget(vp);
	}
}
