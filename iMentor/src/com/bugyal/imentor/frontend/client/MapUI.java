package com.bugyal.imentor.frontend.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

public class MapUI extends Composite {

	private static final String KEY = "ABQIAAAAq8kb0Ptd3vgPpd-4SQhLnRRY5aJ8tdjYBCC6RMf4i7rOl0XHvhRIR_EydegFWQTAB50wMnBjiq9mqw";
	Marker marker;
	MapWidget map;
	private Polygon oldCircle;
	private SliderBar slider = new SliderBar(0.0, 200.0);
	private VerticalPanel panel = new VerticalPanel();

	LocationData lData = new LocationData();

	 private boolean needSlider;
	 private final TextArea locationDisplay;

	public MapUI(boolean needSlider, TextArea locationDisplay) {
		Maps.loadMapsApi(KEY, "2", false, new Runnable() {
			public void run() {
				initMapUI();
			}
		});
		this.needSlider = needSlider;
		this.locationDisplay = locationDisplay;
		initWidget(panel);
	}

	public void initMapUI() {
		map = new MapWidget();
		map.setCenter(LatLng.newInstance(17.45, 78.39, true), 4);
		marker = new Marker(LatLng.newInstance(17.45, 78.39, true));
		map.setSize("585px", "600px");
		map.setCenter(marker.getLatLng());
		MapUIOptions opts = map.getDefaultUI();
		opts.setDoubleClick(false);
		map.setUI(opts);
		map.addOverlay(marker);
		marker.setDraggingEnabled(true);

		if (needSlider) {
		  addMouseOverHandler();
		}
		map.addMapClickHandler(new MapClickHandler() {

			@Override
			public void onClick(MapClickEvent event) {
				if (event != null && event.getLatLng() != null
						&& marker != null) {
					marker.setLatLng(event.getLatLng());
				}
				getAddress(event.getLatLng());
				lData.setLatitude(event.getOverlayLatLng().getLatitude());
				lData.setLatitude(event.getOverlayLatLng().getLongitude());
			}

		});
		panel.add(map);
	}

	private void addMouseOverHandler() {
		marker.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {

			@Override
			public void onMouseOver(MarkerMouseOverEvent event) {
				slider.setStepSize(0.2);
				slider.setCurrentValue(0.0);
				slider.setNumTicks(20);
				slider.setNumLabels(5);
				slider.setSize("300px", "50px");
				drawCircleFromRadius(marker.getLatLng(), slider
						.getCurrentValue() * 1000, 30);

				slider.addChangeListener(new ChangeListener() {

					@Override
					public void onChange(Widget sender) {
						SliderBar sb = (SliderBar) sender;
						if (oldCircle != null) {
							map.removeOverlay(oldCircle);
						}
						drawCircleFromRadius(marker.getLatLng(), sb
								.getCurrentValue() * 1000, 30);
						map.addOverlay(oldCircle);

						lData.setRadius((int) slider.getCurrentValue() * 1000);
					}

				});

				InfoWindowContent iwc = new InfoWindowContent(slider);
				map.getInfoWindow().open(marker.getLatLng(), iwc);

			}

		});
	}

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

		Polygon circle = new Polygon(circlePoints, "green", 0, 0, "#00800", 0.5);
		oldCircle = circle;
	}

	private void getAddress(LatLng point) {
		Geocoder coder = new Geocoder();
		coder.getLocations(point, new LocationCallback() {

			@Override
			public void onFailure(int statusCode) {
				// TODO(ravi): Fill me.
			}

			@Override
			public void onSuccess(JsArray<Placemark> locations) {
				if (locations.length() > 0) {
					lData.setLocation(locations.get(0).getAddress());
					locationDisplay.setText(locations.get(0).getAddress());
				}
			}

		});
	}
}
