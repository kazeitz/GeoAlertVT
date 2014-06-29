package edu.vt.alerts.android.library.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import edu.vt.alerts.android.library.demo.AndroidGeoFence.*;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.GeofenceUtils.REMOVE_TYPE;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.GeofenceUtils.REQUEST_TYPE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


	/**
	 * Created by kazeitz on 2/5/14.
	 * This class contains the functionality provided for the geofences.
	 * It allows for their creation and removal and keeps a record of the current
	 * geofences and a list of their ids for access.
	 */
	public class GeofenceActivity extends FragmentActivity {


	    /*
	    * An instance of an inner class that receives broadcasts from listeners and from the
	    * IntentService that receives geofence transition events
	    */
	    private GeofenceSampleReceiver broadcastReceiver;

	    // An intent filter for the broadcast receiver
	    private IntentFilter intentFilter;

	    // Add geofences handler
	    private GeofenceRequester geofenceRequester;

	    // Remove geofences handler
	    private GeofenceRemover geofenceRemover;

	    /*
	         * Handle results returned to this Activity by other Activities started with
	         * startActivityForResult(). In particular, the method onConnectionFailed() in
	         * GeofenceRemover and GeofenceRequester may call startResolutionForResult() to
	         * start an Activity that handles Google Play services problems. The result of this
	         * call returns here, to onActivityResult.
	         * calls
	         */
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

	        //Get user location
	        // check if GPS enabled
	    	
            if (GlobalApplication.gps.canGetLocation()) {
                double latitude = GlobalApplication.gps.getLatitude();
                double longitude = GlobalApplication.gps.getLongitude();
                LatLng User = new LatLng(latitude, longitude);
                
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                      // Called when a new location is found by the network location provider.
                      //makeUseOfNewLocation(location);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                  };
                
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
            	GlobalApplication.gps.showSettingsAlert();
            }

	        //Test  Add all of the geofences
//	        long expirationHours =12;
//	        double lat = 37.229;
//	        double lng = -80.42371;
//	        float radius = 25;
//	        String id = "1";
//
//	        SimpleGeofence fence = createSimpleGeofence(expirationHours, lat, lng, radius,id);
//	        addSimpleGeofence(fence, context);
//	        addGeofence(fence.toGeofence());

	        // Choose what to do based on the request code
	        switch (requestCode) {

	            // If the request code matches the code sent in onConnectionFailed
	            case GeofenceUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

	                switch (resultCode) {
	                    // If Google Play services resolved the problem
	                    case Activity.RESULT_OK:

	                        // If the request was to add geofences
	                        if (REQUEST_TYPE.ADD == GlobalApplication.requestType) {

	                            // Toggle the request flag and send a new request
	                            geofenceRequester.setInProgressFlag(false);

	                            // Restart the process of adding the current geofences
	                            geofenceRequester.addGeofences(GlobalApplication.currentGeofences);

	                            // If the request was to remove geofences
	                        } else if (REQUEST_TYPE.REMOVE == GlobalApplication.requestType ){

	                            // Toggle the removal flag and send a new removal request
	                            geofenceRemover.setInProgressFlag(false);

	                            // If the removal was by Intent
	                            if (REMOVE_TYPE.INTENT == GlobalApplication.removeType) {

	                                // Restart the removal of all geofences for the PendingIntent
	                                geofenceRemover.removeGeofencesByIntent(
	                                        geofenceRequester.getRequestPendingIntent());

	                                // If the removal was by a List of geofence IDs
	                            } else {

	                                // Restart the removal of the geofence list
	                                geofenceRemover.removeGeofencesById(GlobalApplication.geofenceIDsToRemove);
	                            }
	                        }
	                        break;

	                    // If any other result was returned by Google Play services
	                    default:

	                        // Report that Google Play services was unable to resolve the problem.
	                        Log.d(GeofenceUtils.APPTAG, getString(R.string.no_resolution));
	                }

	                // If any other request code was received
	            default:
	                // Report that this Activity received an unknown requestCode
	                Log.d(GeofenceUtils.APPTAG,
	                        getString(R.string.unknown_activity_request_code, requestCode));

	                break;
	        }
	    }
	  /*
	   * Add Geofence to list
	   */
	    public void createGeofence(long expirationHours, double lat, double lng, float radius, String id, Context context) {

	    	if(GlobalApplication.allGeofencesStore.getGeofence(id) == null){
	    		
	    	System.out.println("Adding id: " + id);

	        /*
	         * Add the geofences to current and store simple geofence
	         */

	        SimpleGeofence simpleFence = createSimpleGeofence(expirationHours, lat, lng, radius, id);

	        // Store this flat version in SharedPreferences
	        addSimpleGeofence(simpleFence, context);

	        /*
	         * Add Geofence objects to a List. toGeofence()
	         * creates a GPSLocation Services Geofence object from a
	         * flat object
	         */
	        addGeofence(simpleFence.toGeofence(),context);
	    	}//end if
	    }
	    /*
	    Request Geofences
	     */
	    public void requestGeofences(){
	        /*
	         * Record the request as an ADD. If a connection error occurs,
	         * the app can automatically restart the add request if Google Play services
	         * can fix the error
	         */
	    	
	    	GlobalApplication.requestType = REQUEST_TYPE.ADD;

	        /*
	         * Check for Google Play services. Do this after
	         * setting the request type. If connecting to Google Play services
	         * fails, onActivityResult is eventually called, and it needs to
	         * know what type of request was in progress.
	         */
	        if (!servicesConnected()) {

	            return;
	        }// Start the request. Fail if there's already a request in progress
	        try {
	            // Try to add geofences
	            geofenceRequester.addGeofences(GlobalApplication.currentGeofences);
	        } catch (UnsupportedOperationException e) {
	            // Notify user that previous request hasn't finished.
	            Toast.makeText(this, R.string.add_geofences_already_requested_error,
	                    Toast.LENGTH_LONG).show();
	        }
	    }

	         /*
	   * Add Geofence to list
	   */
	    public void setUp(Context context){
	    

	        // Set the pattern for the latitude and longitude format
	        String latLngPattern = String.valueOf(R.string.lat_lng_pattern);

	        // Set the format for latitude and longitude
	        GlobalApplication.mLatLngFormat = new DecimalFormat(latLngPattern);

	        // Localize the format
	        GlobalApplication.mLatLngFormat.applyLocalizedPattern(GlobalApplication.mLatLngFormat.toLocalizedPattern());

	        // Set the pattern for the radius format
	        String radiusPattern = String.valueOf(R.string.radius_pattern);

	        // Set the format for the radius
	        GlobalApplication.mRadiusFormat = new DecimalFormat(radiusPattern);

	        // Localize the pattern
	        GlobalApplication.mRadiusFormat.applyLocalizedPattern(GlobalApplication.mRadiusFormat.toLocalizedPattern());

	        // Create a new broadcast receiver to receive updates from the listeners and service
	        broadcastReceiver = new GeofenceSampleReceiver();

	        // Create an intent filter for the broadcast receiver
	        intentFilter = new IntentFilter();

	        // Action for broadcast Intents that report successful addition of geofences
	        intentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);

	        // Action for broadcast Intents that report successful removal of geofences
	        intentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);

	        // Action for broadcast Intents containing various types of geofencing errors
	        intentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);

	        // All GPSLocation Services sample apps use this category
	        intentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

	        // Instantiate a new geofence storage area
	        GlobalApplication.allGeofencesStore = new SimpleGeofenceStore(context);
	        
	        // Instantiate the current List of geofences
	        GlobalApplication.currentGeofences = new ArrayList<Geofence>();

	        // Instantiate a Geofence requester
	        geofenceRequester = new GeofenceRequester(this);

	        // Instantiate a Geofence remover
	        geofenceRemover = new GeofenceRemover(this);
	    }

	    /*
	     * Whenever the Activity resumes, reconnect the client to GPSLocation
	     * Services and reload the last geofences that were set
	     */
	    @Override
	    protected void onResume() {
	        super.onResume();
	        // Register the broadcast receiver to receive status updates
	        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);

	    }
	    /**
	     *  Called when the user clicks the "Remove geofences" button
	     *
	     * @param view The view that triggered this callback
	     */
	    public void onUnregisterByPendingIntentClicked(View view) {
	        /*
	         * Remove all geofences set by this app. To do this, get the
	         * PendingIntent that was added when the geofences were added
	         * and use it as an argument to removeGeofences(). The removal
	         * happens asynchronously; GPSLocation Services calls
	         * onRemoveGeofencesByPendingIntentResult() (implemented in
	         * the current Activity) when the removal is done
	         */

	        /*
	         * Record the removal as remove by Intent. If a connection error occurs,
	         * the app can automatically restart the removal if Google Play services
	         * can fix the error
	         */
	        // Record the type of removal
	    	
	    	GlobalApplication.removeType = REMOVE_TYPE.INTENT;

	        /*
	         * Check for Google Play services. Do this after
	         * setting the request type. If connecting to Google Play services
	         * fails, onActivityResult is eventually called, and it needs to
	         * know what type of request was in progress.
	         */
	        if (!servicesConnected()) {

	            return;
	        }

	        // Try to make a removal request
	        try {
	        /*
	         * Remove the geofences represented by the currently-active PendingIntent. If the
	         * PendingIntent was removed for some reason, re-create it; since it's always
	         * created with FLAG_UPDATE_CURRENT, an identical PendingIntent is always created.
	         */
	            geofenceRemover.removeGeofencesByIntent(geofenceRequester.getRequestPendingIntent());

	        } catch (UnsupportedOperationException e) {
	            // Notify user that previous request hasn't finished.
	            Toast.makeText(this, R.string.remove_geofences_already_requested_error,
	                    Toast.LENGTH_LONG).show();
	        }

	    }

	    /*
	     * Create a version of geofence that is "flattened" into individual fields. This
	     * allows it to be stored
	     */
	    public SimpleGeofence createSimpleGeofence(long expirationHours, double lat, double lng, float radius, String id){

	        //Put expiration hours into milliseconds
	        long expirationMillis = expirationHours * DateUtils.HOUR_IN_MILLIS;

	        SimpleGeofence simple = new SimpleGeofence(
	                id,
	                lat,
	                lng,
	                radius,
	                // Set the expiration time
	                expirationMillis,
	                // Detect both entry and exit transitions
	                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT
	        );
	        return simple;
	    }

	    /*
	     * Add Geofence to list
	     */
	    public void addGeofence(Geofence fence, Context context){
	    	
	    	//Save the geofence
	    	 if(GlobalApplication.currentGeofences == null){
	    		 GlobalApplication.currentGeofences = new ArrayList<Geofence>();
		        }
	    	 GlobalApplication.currentGeofences.add(fence);
	    }
	    /*
	     * Remove Geofence from list
	     */
	    public void removeGeofence(Geofence fence){
	    	
	        //Get the id
	        String id = fence.getRequestId();

	        //Add to list
	        GlobalApplication.geofenceIDsToRemove = Collections.singletonList(id);

	        /*
	         * Record the removal as remove by list. If a connection error occurs,
	         * the app can automatically restart the removal if Google Play services
	         * can fix the error
	         */
	        GlobalApplication.removeType = REMOVE_TYPE.LIST;
	         /*
	         * Check for Google Play services. Do this after
	         * setting the request type. If connecting to Google Play services
	         * fails, onActivityResult is eventually called, and it needs to
	         * know what type of request was in progress.
	         */
	        if (!servicesConnected()) {

	            return;
	        }

	        // Try to remove the geofence
	        try {
	            geofenceRemover.removeGeofencesById(GlobalApplication.geofenceIDsToRemove);

	            // Catch errors with the provided geofence IDs
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (UnsupportedOperationException e) {
	            // Notify user that previous request hasn't finished.
	            Toast.makeText(this, R.string.remove_geofences_already_requested_error,
	                    Toast.LENGTH_LONG).show();
	        }
	    }

	   /*
	    * Add Simple Geofence to list
	    */
	    public void addSimpleGeofence(SimpleGeofence fence, Context context){
	    	
	        if(GlobalApplication.allGeofencesStore == null){
	        	GlobalApplication.allGeofencesStore = new SimpleGeofenceStore(context);
	        }
	    	//Save the simple geofence
	        GlobalApplication.allGeofencesStore.setGeofence(fence.getId(),fence);
	        //Save the ID
	        if(GlobalApplication.allGeofenceIDs == null){
	        	GlobalApplication.allGeofenceIDs = new ArrayList<String>();
	        }
	        GlobalApplication.allGeofenceIDs.add(fence.getId());
	    }

	    /*
	     * Remove Simple Geofence from list
	     */
	    public void removeSimpleGeofence(String id){
	    	
	        //Remove the simple geofence
	    	GlobalApplication.allGeofencesStore.clearGeofence(id);
	        //Remove the ID
	    	GlobalApplication.allGeofenceIDs.remove(id);
	    }

	   /*
	   * Get Simple Geofences
	   */
	    public SimpleGeofenceStore getSimpleGeofences(){
	    
	        return GlobalApplication.allGeofencesStore;
	    }
	    /**
	     * Get Geofence Ids
	     */
	    public List<String> getGeofenceIDs(){
	    
	        return GlobalApplication.allGeofenceIDs;
	    }
	    /**
	     * Check Google Play Services to make sure it is available
	     * If it is available return true, otherwise return false
	     */
	    public boolean servicesConnected(){
	        // Check that Google Play services is available
	        int resultCode =
	                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

	        // If Google Play services is available
	        if (ConnectionResult.SUCCESS == resultCode) {

	            // In debug mode, log the status
	            Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));

	            // Continue
	            return true;

	            // Google Play services was not available for some reason
	        } else {

	            // Display an error dialog
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
	            if (dialog != null) {
	                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
	                errorFragment.setDialog(dialog);
	                errorFragment.show(getSupportFragmentManager(), GeofenceUtils.APPTAG);
	            }
	            return false;
	        }
	    }

	    /**
	     * Define a Broadcast receiver that receives updates from connection listeners and
	     * the geofence transition service.
	     */
	    public class GeofenceSampleReceiver extends BroadcastReceiver {
	        /*
	         * Define the required method for broadcast receivers
	         * This method is invoked when a broadcast Intent triggers the receiver
	         */
	        @Override
	        public void onReceive(Context context, Intent intent) {

	            //Toast.makeText(context, "YOU ARE IN OnReceive", Toast.LENGTH_LONG).show();

	            // Check the action code and determine what to do
	            String action = intent.getAction();

	            // Intent contains information about errors in adding or removing geofences
	            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

	                handleGeofenceError(context, intent);

	                // Intent contains information about successful addition or removal of geofences
	            } else if (
	                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
	                            ||
	                            TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

	                handleGeofenceStatus(context, intent);

	                // Intent contains information about a geofence transition
	            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

	                handleGeofenceTransition(context, intent);

	                // The Intent contained an invalid action
	            } else {
	                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
	                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
	            }
	        }

	        /**
	         * If you want to display a UI message about adding or removing geofences, put it here.
	         *
	         * @param context A Context for this component
	         * @param intent The received broadcast Intent
	         */
	        private void handleGeofenceStatus(Context context, Intent intent) {

	        }

	        /**
	         * Report geofence transitions to the UI
	         *
	         * @param context A Context for this component
	         * @param intent The Intent containing the transition
	         */
	        private void handleGeofenceTransition(Context context, Intent intent) {
	            /*
	             * If you want to change the UI when a transition occurs, put the code
	             * here. The current design of the app uses a notification to inform the
	             * user that a transition has occurred.
	             */
	        	//On a user location change update the map
	        	//May need to tweak how often this occurs and when (Right now only if an alert is sent)
	        	//Need to reset the map
	        	Intent in = new Intent(context,MapActivity.class);
	            startActivity(in);
	        	
	        }

	        /**
	         * Report addition or removal errors to the UI, using a Toast
	         *
	         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
	         */
	        private void handleGeofenceError(Context context, Intent intent) {
	            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
	            Log.e(GeofenceUtils.APPTAG, msg);
	            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	        }
	    }
	    /**
	     * Define a DialogFragment to display the error dialog generated in
	     * showErrorDialog.
	     */
	    public static class ErrorDialogFragment extends DialogFragment {

	        // Global field to contain the error dialog
	        private Dialog mDialog;

	        /**
	         * Default constructor. Sets the dialog field to null
	         */
	        public ErrorDialogFragment() {
	            super();
	            mDialog = null;
	        }

	        /**
	         * Set the dialog to display
	         *
	         * @param dialog An error dialog
	         */
	        public void setDialog(Dialog dialog) {
	            mDialog = dialog;
	        }

	        /*
	         * This method must return a Dialog to the DialogFragment.
	         */
	        @Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	            return mDialog;
	        }
	    }

	}//end class GeofenceActivity

