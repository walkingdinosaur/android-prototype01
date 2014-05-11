package com.example.prototype01;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;

public class StateSave {
	public static final String DEFAULT_PREF_NAME = "DEFAULT_PREF_NAME";
	public static final String LAST_LAT = "last_lat";
	public static final String LAST_LON = "last_lon";
	public static final String LAST_ZOOMLEVEL = "last_zoomlevel";

	private GeoPoint geoPoint;
	private int zoomLevel;

	public GeoPoint getLastGeoPoint() {
		return this.geoPoint;
	}

	public int getLastZoomLevel() {
		return this.zoomLevel;
	}

	public static SharedPreferences getDefaultSharedPrefernces(Context context) {
		return context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE);
	}

	public static StateSave getLastState(Context context) {
		SharedPreferences sp = getDefaultSharedPrefernces(context);

		int lat = sp.getInt(LAST_LAT, 0);
		int lon = sp.getInt(LAST_LON, 0);
		int zoomLevel = sp.getInt(LAST_ZOOMLEVEL, 0);

		if (0 == lat || 0 == lon || 0 == zoomLevel) {
			return null;
		}
		StateSave state = new StateSave();
		state.zoomLevel = zoomLevel;
		state.geoPoint = new GeoPoint(lat, lon);

		return state;
	}

	public static void saveState(Context context, MapView mapView) {
		MapController mc = mapView.getMapController();
		GeoPoint geoPoint = mc.getMapCenter();
		int zoomLevel = mapView.getZoomLevel();

		SharedPreferences sp = getDefaultSharedPrefernces(context);

		Editor edit = sp.edit();
		edit.putInt(LAST_LAT, geoPoint.getLatitudeE6());
		edit.putInt(LAST_LON, geoPoint.getLongitudeE6());
		edit.putInt(LAST_ZOOMLEVEL, zoomLevel);
		edit.apply();
	}
}
