package edu.vt.alerts.android.library.demo;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Application;

import com.google.android.gms.location.Geofence;

import edu.vt.alerts.android.library.demo.AndroidGeoFence.SimpleGeofenceStore;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.GeofenceUtils.REMOVE_TYPE;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.GeofenceUtils.REQUEST_TYPE;
import edu.vt.alerts.android.library.demo.adapters.AlertSummaryAdapter;

public class GlobalApplication extends Application {
	
	  //decimal formats for latitude, longitude, and radius
	  public static DecimalFormat mLatLngFormat;
	  public static DecimalFormat mRadiusFormat;

	  // Persistent storage for geofences
	  public static SimpleGeofenceStore allGeofencesStore;

	  // List of ids for persistent storage for geofences
	  public static List<String> allGeofenceIDs; 

	  // Store a list of geofences
	  public static List<Geofence> currentGeofences;

	  // Store a list of geofence ids to remove
	  public static List<String> geofenceIDsToRemove;

	  //Store the current request
	  public static REQUEST_TYPE requestType;

	  //Store the current removal type
	  public static REMOVE_TYPE removeType;
	  
	//Variables for location
	public static GPSLocation gps;
	
	//List of alerts
	public static AlertSummaryAdapter adapter;
	
	//How close to determine nearby in meters
	public static double nearbyRange = 1500;

}
