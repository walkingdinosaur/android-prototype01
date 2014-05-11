package com.example.prototype01;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements MapViewListener, LocationListener {
	public static final String TAG = "proto01";

	private static final String appid = "dj0zaiZpPWN6ZXBYYzNWQ2hISyZkPVlXazlNMWh3VUdZMk5qSW1jR285TUEtLSZzPWNvbnN1bWVyc2VjcmV0Jng9MDU-";
	private MapView mapView;
	private MapController mc;
	private LocationManager locationManager;
	private MapViewListener mMapViewListener;
	private Timer timeOut;
	private TextView locationValues = null;

	private static final int MENUID_START = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		this.setContentView(R.layout.main_layout);

		// if (savedInstanceState == null) {
		// this.getSupportFragmentManager().beginTransaction().add(R.id.container,
		// new PlaceholderFragment()).commit();
		// }
		this.initializeMap();

		this.locationValues = (TextView) this.findViewById(R.id.location_change);
	}

	private void initializeMap() {
		this.mapView = new MapView(this, appid);
		this.mc = this.mapView.getMapController();
		this.mc.setCenter(new GeoPoint(35665721, 139731006));
		this.mc.setZoom(1);
		this.mapView.addMapViewListener(this);

		RelativeLayout ymapLayout = (RelativeLayout) findViewById(R.id.ymap);
		ymapLayout.addView(this.mapView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.start_location) {
			this.locationStart();
		} else if (id == R.id.stop_location) {
			this.locationStop();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		StateSave state = StateSave.getLastState(getApplicationContext());
		if (null != state) {
			this.mc.setCenter(state.getLastGeoPoint());
			this.mc.setZoom(state.getLastZoomLevel());
		}

		super.onResume();
	}

	@Override
	public boolean onChangeMap(MapView arg0) {
		StateSave.saveState(getApplicationContext(), this.mapView);
		return false;
	}

	@Override
	public boolean onChangeScale(MapView arg0) {
		StateSave.saveState(getApplicationContext(), this.mapView);
		return false;
	}

	@Override
	public void onSendAd() {
		// TODO Auto-generated method stub
		
	}

	private void timeoutOn() {
		if (this.timeOut != null) {
			this.timeoutOff();
		}

		long delay = 10L * 1000L;
		this.timeOut = new Timer();
		this.timeOut.schedule(new TimerTask() {
			@Override
			public void run() {
			}
		}, delay);
	}

	private void timeoutOff() {
		if (this.timeOut == null) {
			return;
		}
		this.timeOut.cancel();
		this.timeOut = null;
	}

	private void locationStop() {
		if (this.locationManager == null) {
			return;
		}
		this.locationManager.removeUpdates(this);
		TextView locationStat = (TextView) this.findViewById(R.id.location_status);
		locationStat.setText("Location STOP");
	}

	private void locationStart() {
		if (this.locationManager == null) {
			this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}

		long minTime = 5000L;
		float minDistance = (float) 5.0;
		List<String> providers = this.locationManager.getAllProviders();
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> ite = providers.iterator(); ite.hasNext(); ) {
			String provider = ite.next();
			Log.d(TAG, "add provider: " + provider);
			this.locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
			builder.append(provider);
			builder.append(",");
		}

		TextView locationStat = (TextView) this.findViewById(R.id.location_status);
		locationStat.setText("Location START provider: " + builder.substring(0, builder.length()-1));
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "lat: " + location.getLatitude() + " lon:" + location.getLongitude());

		int lat = 	(int) (location.getLatitude() * 1E6);
		int lon = 	(int) (location.getLongitude() * 1E6);
		GeoPoint geoPoint = new GeoPoint(lat, lon);
		this.mc.animateTo(geoPoint);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}
}
