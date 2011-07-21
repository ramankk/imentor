package com.bugyal.imentor.frontend.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

public class MapUIWidget extends Composite{

	MapWidget map = new MapWidget(LatLng.newInstance(17.45, 78.39), 10);
	Marker marker = new Marker(LatLng.newInstance(17.45, 78.39, true));
	
	public MapUIWidget(boolean b)
	{
		
		map.clearOverlays();	
		map.setSize("585px", "318px");
		map.setCenter(marker.getLatLng());
		MapUIOptions opts = map.getDefaultUI();
		opts.setDoubleClick(false);
		map.setUI(opts);
		map.addOverlay(marker);

		marker.setDraggingEnabled(true);
		if (b) {
			marker.addMarkerMouseOverHandler(new MarkerMouseOverHandler()
			{

				@Override
				public void onMouseOver(MarkerMouseOverEvent event) {
					Button ok = new Button("OK");
					InfoWindowContent iwc = new InfoWindowContent(
							"Press Ok button to save your location" + ok);
					InfoWindow info = map.getInfoWindow();
					info.open(event.getSender(), iwc);
					ok.addClickHandler(new ClickHandler()
					{

						@Override
						public void onClick(ClickEvent event) {
							Window.alert("Your location has been saved");
							
						}
						
					});
					
				}
				
			});
			
		} else {
			map.clearOverlays();
			// TO DO - Ravi Teja
		}
		map.addMapClickHandler(new MapClickHandler()
		{

			@Override
			public void onClick(MapClickEvent event) {
				map.setCenter(event.getLatLng());
				marker.setLatLng(event.getLatLng());
			//	mdo.setLatLng(event.getLatLng());
				
			}
			
		});
		
		initWidget(map);
	}
}
