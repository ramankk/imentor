package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Comment extends DialogBox {

	MentorServiceAsync service;
	TextBox subject;
	TextArea comment;

	public Comment() {
		setHTML("Feedback");

		service = (MentorServiceAsync) GWT.create(MentorService.class);

		VerticalPanel verticalPanel = new VerticalPanel();
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		horizontalPanel.add(new Label("Subject :"));

		subject = new TextBox();
		horizontalPanel.add(subject);
		subject.setWidth("150px");
		comment = new TextArea();
		verticalPanel.add(comment);
		comment.setSize("256px", "100px");
		HorizontalPanel hp = new HorizontalPanel();
		Button submit = new Button("Submit");
		Button cancel = new Button("Cancel");
		submit.addClickHandler(new ClickHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(ClickEvent event) {
				service.commment(subject.getText(), comment.getText(),
						new AsyncCallback() {

							@Override
							public void onFailure(Throwable caught) {
								MainPageWidget
										.setErrorMessage("Unable to post your comment!");
							}

							@Override
							public void onSuccess(Object result) {
								dialogHide();
								MainPageWidget
										.setMessage("Comment posted successfully");
							}

						});

			}

		});
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogHide();
			}

		});
		hp.setWidth("80px");
		hp.add(submit);
		hp.add(cancel);
		verticalPanel.add(hp);
		verticalPanel.setCellHorizontalAlignment(hp,
				HasHorizontalAlignment.ALIGN_RIGHT);
	}

	private void dialogHide() {
		this.hide();
	}
}
