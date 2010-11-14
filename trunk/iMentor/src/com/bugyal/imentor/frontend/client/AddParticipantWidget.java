package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.OpportunityVO;
import com.bugyal.imentor.frontend.shared.ParticipantVO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

public class AddParticipantWidget extends Composite {

	private static final String KEY = "ABQIAAAAq8kb0Ptd3vgPpd-4SQhLnRRY5aJ8tdjYBCC6RMf4i7rOl0XHvhRIR_EydegFWQTAB50wMnBjiq9mqw";

	private FlexTable table;

	private Label nameL;
	private TextBox name;

	private Label subjectsL;
	private ListBox subjects;

	private Label locationL;
	private MapWidget map;

	private Button save;
	private Button cancel;

	private Marker marker;
	private SliderBar slider = new SliderBar(0.0, 200.0);
	private String email = "";

	private MentorServiceAsync rpcService;

	public AddParticipantWidget(String email, MentorServiceAsync rpc) {
		table = new FlexTable();
		this.rpcService = rpc;
		this.email = email;

		Maps.loadMapsApi(KEY, "2", false, new Runnable() {
			public void run() {
				buildWidget();
			}
		});
		initWidget(table);
	}

	private void buildWidget() {
		name = new TextBox();
		nameL = new Label("Name : ");
		subjectsL = new Label("Known subjects : ");
		subjects = new ListBox();

		locationL = new Label("Mark your Location");
		save = new Button("Save");
		cancel = new Button("Cancel");

		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ParticipantVO p = new ParticipantVO(null, name.getText(),
						email, marker.getLatLng().getLatitude(), marker
								.getLatLng().getLongitude(), "locString",
						(int) slider.getCurrentValue(), getSelectedSubjects(),
						null);
				rpcService.create(p, new AsyncCallback<ParticipantVO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed " + caught.getMessage());
					}

					@Override
					public void onSuccess(ParticipantVO result) {
						Window.alert("Created !! ,  now searching... " );
						rpcService.find(null, result,
								new AsyncCallback<List<OpportunityVO>>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Failed ... " + caught.getMessage());
									}

									@Override
									public void onSuccess(
											List<OpportunityVO> result) {
										Window.alert("Searched !! , got responses :: " + result.size());
										RootPanel.get("log").add(
												new HTML(result.get(0)
														.toString()));
									}
								});
					}
				});
			}

			private List<String> getSelectedSubjects() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		map = new MapWidget(LatLng.newInstance(17.45, 78.39), 10);
		marker = new Marker(map.getCenter());

		map.setSize("800px", "500px");

		// Workaround for bug with click handler & setUItoDefaults() - see issue
		// 260
		MapUIOptions opts = map.getDefaultUI();
		opts.setDoubleClick(false);
		map.setUI(opts);
		map.addOverlay(marker);

		map.addMapClickHandler(new MapClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(MapClickEvent e) {
				MapWidget sender = e.getSender();
				Overlay overlay = e.getOverlay();
				LatLng point = e.getLatLng();

				if (overlay != null && overlay instanceof Marker) {
					sender.removeOverlay(overlay);
				} else {
					marker.setLatLng(point);
					HorizontalPanel hp = new HorizontalPanel();
					Button b = new Button("Ok");
					slider.setStepSize(20.0);
					slider.setCurrentValue(50.0);
					slider.setNumTicks(20);
					slider.setNumLabels(5);
					slider.setSize("300px", "50px");
					drawCircleFromRadius(marker.getLatLng(), slider
							.getCurrentValue() * 1000, 30);

					slider.addChangeListener(new ChangeListener() {

						@Override
						public void onChange(Widget sender) {
							SliderBar sb = (SliderBar) sender;
							drawCircleFromRadius(marker.getLatLng(), sb
									.getCurrentValue() * 1000, 30);
						}
					});

					hp.add(slider);
					hp.add(b);
					InfoWindowContent iwc = new InfoWindowContent(hp);
					sender.getInfoWindow().open(point, iwc);
				}
			}
		});

		table.setWidget(0, 0, nameL);
		table.setWidget(0, 1, name);

		table.setWidget(1, 0, locationL);
		table.setWidget(1, 1, map);

		table.setWidget(2, 0, subjectsL);
		table.setWidget(2, 1, subjects);

		table.setWidget(3, 0, cancel);
		table.setWidget(3, 1, save);
	}

	private Polygon oldCircle;

	public void drawCircleFromRadius(LatLng center, double radius,
			int nbOfPoints) {

		LatLngBounds bounds = LatLngBounds.newInstance();
		LatLng[] circlePoints = new LatLng[nbOfPoints];

		double EARTH_RADIUS = 6371000;
		double d = radius / EARTH_RADIUS;
		double lat1 = Math.toRadians(center.getLatitude());
		double lng1 = Math.toRadians(center.getLongitude());

		double a = 0;
		double step = 360.0 / (double) nbOfPoints;
		for (int i = 0; i < nbOfPoints; i++) {
			double tc = Math.toRadians(a);
			double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d)
					+ Math.cos(lat1) * Math.sin(d) * Math.cos(tc));
			double lng2 = lng1
					+ Math.atan2(Math.sin(tc) * Math.sin(d) * Math.cos(lat1),
							Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));
			LatLng point = LatLng.newInstance(Math.toDegrees(lat2), Math
					.toDegrees(lng2));
			circlePoints[i] = point;
			bounds.extend(point);
			a += step;
		}

		if (oldCircle != null) {
			map.removeOverlay(oldCircle);
		}
		Polygon circle = new Polygon(circlePoints, "white", 0, 0, "green", 0.5);
		oldCircle = circle;
		map.addOverlay(circle);
	}

}
