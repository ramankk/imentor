package com.bugyal.imentor.frontend.client;

import java.util.ArrayList;
import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchResponseWidget extends Composite {

	private int pageSize;
	private List<SearchResultWidget> resultObjects = new ArrayList<SearchResultWidget>();
	private List<SearchResult> searchResults = new ArrayList<SearchResult>();
	
	private VerticalPanel verticalPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
	private Button forwardButton = new Button(">");
	private Button backwardButton = new Button("<");
	
	private int currentPage;

	public SearchResponseWidget() {
		this(7); // default size
	}
	
	public SearchResponseWidget(int size) {
		this.pageSize = size;

		for (int i = 0; i < pageSize; i++) {
			resultObjects.add(new SearchResultWidget(i % 2 == 0));
			contentPanel.add(resultObjects.get(i));
		}
		
		verticalPanel.setSize("720px", "235px");

		contentPanel.setSize("580px", "180px");
		verticalPanel.add(contentPanel);
		DOM.setStyleAttribute(contentPanel.getElement(), "border",
				"1px solid #5CB3FF");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		horizontalPanel.add(backwardButton);
		backwardButton.setEnabled(false);
		horizontalPanel.add(forwardButton);
		
		backwardButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPage(currentPage - 1);
			}
		});

		forwardButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPage(currentPage + 1);
			}
		});
		
		initWidget(verticalPanel);
	}
	
	public void setResults(List<SearchResult> participants) {
		searchResults = participants;
		showPage(0);
	}
	
	public void showPage(int page) {
		if (page < 0) {
			return;
		}
		currentPage = page;
		int i;
		for (i = 0; i < pageSize; i++) {
			if (((page * pageSize) + i) < searchResults.size()) {
				resultObjects.get(i).setResult(searchResults.get((page * pageSize) + i));
			} else {
				resultObjects.get(i).clear();
			}
		}
		backwardButton.setEnabled(currentPage != 0);

		// Need to check it out by sridhar...!
		int numPages = searchResults.size()/pageSize;
		if(numPages == 0 || currentPage == numPages - 1) {
			forwardButton.setEnabled(false);
		} else{
			forwardButton.setEnabled(true);
		}
	}
}
