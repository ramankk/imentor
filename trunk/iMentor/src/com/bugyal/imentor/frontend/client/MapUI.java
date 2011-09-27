package com.bugyal.imentor.frontend.client;

import java.util.List;

import com.bugyal.imentor.frontend.shared.SearchResult;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

@SuppressWarnings("deprecation")
public class MapUI extends Composite {

	private static final String TEST_RAVI_KAWANAN_APPSPOT_KEY = "ABQIAAAALzeF9m4Hkx6Z37BVvHWvxhShoPpDNRXYgkj2XkGsfA3OCpQSpBR6ojdq8bueS3AunWouo4SxLrUkkQ";
	private static final String KAWANAN_KEY = "ABQIAAAALzeF9m4Hkx6Z37BVvHWvxhTxjsHT9BqImXAXxpnCBoIw1Gf1ABTWIbbDusif-1jMhT5SvnYRpn42KQ";
	private static final String DEV_KEY = "ABQIAAAAq8kb0Ptd3vgPpd-4SQhLnRRY5aJ8tdjYBCC6RMf4i7rOl0XHvhRIR_EydegFWQTAB50wMnBjiq9mqw";
	Marker marker;
	MapWidget map;
	private Polygon oldCircle;
	private SliderBar slider = new SliderBar(0.0, 200.0);
	private VerticalPanel panel = new VerticalPanel();
	// private String partSubjects = "";
	LocationData lData = new LocationData();

	private boolean needSlider;
	private final TextArea locationDisplay;

	public MapUI(boolean needSlider, TextArea locationDisplay) {
		Maps.loadMapsApi(TEST_RAVI_KAWANAN_APPSPOT_KEY, "2", false,
				new Runnable() {
					public void run() {
						initMapUI();
					}
				});
		this.needSlider = needSlider;
		this.locationDisplay = locationDisplay;
		initWidget(panel);
	}

	public void addPartMarkers(int index, List<SearchResult> response) {
		LatLng searchLL = marker.getLatLng();
		map.clearOverlays();
		for (final SearchResult record : response) {

			LatLng ll = LatLng.newInstance(record.getLatitude(), record
					.getLongitude());
			Marker partMarker = new Marker(ll);
			map.addOverlay(partMarker);
			partMarker.setDraggingEnabled(false);

			StringBuilder partSubjects = new StringBuilder();

			switch (index) {
			case 0:
				if (record.isTypeParticipant()) {
					partSubjects.append(record.getP().getName());
					partSubjects.append("<br /> Needs : ");
					partSubjects
							.append(record.getP().getNeedSubjectsAsString());
					partSubjects.append("<br /> And He has : ");
					partSubjects.append(record.getP().getHasSubjectsAsString());
					partSubjects.append("<br /> more...");
					Image image = new Image("images/magni.png");
					image.setPixelSize(15, 15);
					partSubjects.append(image);
				} else {
					partSubjects.append(record.getO().getLocString());
					partSubjects.append("<br /> Opportunity Needs : ");
					partSubjects.append(record.getO().getSubjectsAsString());
				}
				partMarker.setImage("images/marker.png");
				break;

			case 1:
				partSubjects.append(record.getP().getName());
				partSubjects.append("<br /> Has : ");
				partSubjects.append(record.getP().getHasSubjectsAsString());
				partMarker.setImage("images/marker1.png");
				break;

			case 2:
				if (record.isTypeParticipant()) {
					partSubjects.append(record.getP().getName());
					partSubjects.append(" Needs : ");
					partSubjects
							.append(record.getP().getNeedSubjectsAsString());
				} else {
					partSubjects.append(record.getO().getLocString());
					partSubjects.append(" Opportunity Needs : ");
					partSubjects.append(record.getO().getSubjectsAsString());
				}
				partMarker.setImage("images/marker.png");
				break;

			default:
				Window.alert("Somethig Wrong with you selection");
			}
			final String temp = partSubjects.toString();
			partMarker.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {

				@Override
				public void onMouseOver(MarkerMouseOverEvent event) {
					InfoWindowContent iwc = new InfoWindowContent(temp);
					InfoWindow info = map.getInfoWindow();
					info.open(event.getSender(), iwc);
				}
			});
		}
		Marker searchMarker = new Marker(searchLL);
		map.addOverlay(searchMarker);
	}

	public void initMapUI() {
		map = new MapWidget();
		map.setSize("700px", "600px");

		map.setCenter(LatLng.newInstance(17.45, 78.39, true), 4);
		marker = new Marker(LatLng.newInstance(17.45, 78.39, true));
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

				marker.setLatLng(event.getLatLng());
				getAddress(event.getLatLng());
				lData.setLatitude(marker.getLatLng().getLatitude());
				lData.setLongitude(marker.getLatLng().getLongitude());
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

	public LocationData getLocationDetails() {
		return lData;
	}
	public void setMarkerLocation(double lat, double lng, int radius) {
		lData.setLatitude(lat);
		lData.setLongitude(lng);
		lData.setRadius(radius);

		LatLng ll = LatLng.newInstance(lat, lng);
		map.setCenter(ll, 9);
		marker.setLatLng(ll);
	}

	public void setZoomLevelToKm(int km) {
		map.setZoomLevel(getZoomLevelByKM(km));
	}

	public void clear() {
		map.clearOverlays();
		marker = new Marker(LatLng.newInstance(lData.getLatitude(), lData.getLongitude(), true));
		map.addOverlay(marker);
	}

	public void setLocationDetails(LocationData lData) {
		this.lData = lData;
	}
	
	public int getZoomLevelByKM(int km){
		if(km < 0.3)
			return 17;
		if(km < 0.8)
			return 16;
		if(km < 1.6)
			return 15;
		if(km < 3.2)
			return 14;
		if(km < 4.8)
			return 13;
		if(km < 11)
			return 12;
		if(km < 24)
			return 11;
		if(km < 48)
			return 10;
		if(km < 96)
			return 9;
		if(km < 192)
			return 8;
		if(km < 384)
			return 7;
		if(km < 768)
			return 6;
		if(km < 1536)
			return 5;
		if(km < 3072)
			return 4;
		if(km < 6144)
			return 3;
		if(km < 12288)
			return 2;
		else return 9;
		
	}
}
