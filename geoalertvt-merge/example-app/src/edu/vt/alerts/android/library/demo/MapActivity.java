package edu.vt.alerts.android.library.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;
import edu.vt.alerts.android.library.demo.R.string;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.*;
import edu.vt.alerts.android.library.demo.adapters.AlertSummaryAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.maps.android.SphericalUtil;

//import java.util.Iterator;
//import java.util.List;

//public class MapActivity extends ActionBarActivity implements View.OnClickListener   {
public class MapActivity extends BaseActivity implements View.OnClickListener   {

    Button viewButton;
    Button alertButton;
    boolean nearby = true;
    Context context = this;

    //Variables for the google map
    private GoogleMap map;
   
   //Test variables
    LatLng User = new LatLng(37.229, -80.42371); //Default at center of campus if no user location
   // LatLng Torg = new LatLng(37.22978, -80.41997);
   // LatLng Lane = new LatLng(37.21997, -80.41873);

    //Variables for location
    GPSLocation gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //GoogleMap
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        
        //Set the user location
        map.setMyLocationEnabled(true);
        
        map.setOnMarkerClickListener(new OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker mark) {
            	int position = 0;
            	//Get id (URL) to match
            	String id = mark.getTitle();
            	
            	//Get the alert position
            	for(int i=0; i < GlobalApplication.adapter.getCount(); i++){
            		AlertSummary summary = GlobalApplication.adapter.getItem(i);
            		String url = summary.getUrl();
            		if(url.equals(id)){
            			position = i;
            		}
            	}
            	//Start the intent
            	AlertSummary summary = GlobalApplication.adapter.getItem(position);
                Intent intent = new Intent(MapActivity.this, AlertActivity.class);
                intent.putExtra(AlertActivity.ALERT_EXTRA, summary);
                startActivity(intent);
                
				return true;
            }
        });   
        
        viewButton = (Button) findViewById(R.id.map_view);
        viewButton.setOnClickListener(new View.OnClickListener() {
 		    @Override
 		    public void onClick(View view) {
 		    	int normalView = map.MAP_TYPE_NORMAL;
 		    	if (map.getMapType() == map.MAP_TYPE_NORMAL){
 		    		viewButton.setText(string.map_mapView);
 	                map.setMapType(map.MAP_TYPE_SATELLITE);
 		    	}
 	            else{
 	            	viewButton.setText(string.map_earthView);
 	            	map.setMapType(map.MAP_TYPE_NORMAL);
 		        }
 		    }
 		});
        
        //Toggle the geofences
        alertButton = (Button) findViewById(R.id.map_alerts);
        alertButton.setOnClickListener(new View.OnClickListener() {
 		    @Override
 		    public void onClick(View view) {
 		    	
 		    	if (nearby){
 		    		nearby = false;
 		    		alertButton.setText(string.map_nearbyAlerts);
 		    		map.clear();
 		    		
 		    		//Draw nearby markers
 		    		DisplayAllGeofences();
 		                  
 		    	}
 	            else{
 	            	nearby = true;
 	            	alertButton.setText(string.map_allAlerts);
 	            	map.clear();
 	            	DisplayNearbyGeofences();
		            
 		        }
 		    }
 		});

        //Display geofences
        if (map!=null){
            
            gps = new GPSLocation(this);

            // check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                User = new LatLng(latitude, longitude);
                
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
                gps.showSettingsAlert();
            }

            DisplayAllGeofences();

            // Move the camera instantly to the user with a zoom of 30.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(User, 30));

            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
        
    }
    //Allow for the user to go back to the alert display with button
    private void returnButtonClick(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
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
        }
       
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_map, container, false);
            return rootView;
        }
    }
    /**
     * A method to display all of the current geofences
     */
    public void DisplayAllGeofences(){
        GeofenceActivity geoActivity = new GeofenceActivity();
        //SimpleGeofenceStore geofences = geoActivity.getSimpleGeofences();

        /*Testing markers
         Marker  bur= map.addMarker(new MarkerOptions().position(Torg)
                .title("Torg"));
        
      //Draw circles for each geofence
        Circle circ = map.addCircle(new CircleOptions()
                .center(Torg)
                .radius(50)
                .strokeColor(Color.RED)
                .strokeWidth(5)
                .fillColor(0x40ff0000)   //Semi-transparent
        );*/
        
        //If fence is null do nothing
        if(GlobalApplication.allGeofencesStore == null){
            return;
        }
        else {
            //Get the list of IDs
           //List<String> ids = GlobalApplication.allGeofenceIDs;
        	AlertSummaryAdapter alerts = GlobalApplication.adapter;

            //Create iterator to traverse the Geofences
            //Iterator<String> iterator = ids.iterator();
            int count = alerts.getCount();
        	
            //while (iterator.hasNext()) {
        	for(int i=0;i<count;i++){
        		
                //Get the geofence with the id
        		AlertSummary item = alerts.getItem(i);
        		String url = item.getUrl();
                SimpleGeofence fence = GlobalApplication.allGeofencesStore.getGeofence(url);

                //Set the LatLng
                LatLng fenceLatLng = new LatLng(fence.getLatitude(),fence.getLongitude());
                
                Marker alert = map.addMarker(new MarkerOptions().position(fenceLatLng)
                        .title(fence.getId())
                        .snippet(fence.getId()));

                //Draw circles for each geofence
                Circle circle = map.addCircle(new CircleOptions()
                        .center(fenceLatLng)
                        .radius(fence.getRadius())
                        .strokeColor(Color.RED)
                        .strokeWidth(5)
                        .fillColor(0x40ff0000)   //Semi-transparent
                );
            }
        }
    }
    
    /**
     * A method to display all of the nearby geofences
     */
    public void DisplayNearbyGeofences(){
        GeofenceActivity geoActivity = new GeofenceActivity();
        //The variable for how close we want to consider nearby
        double range = GlobalApplication.nearbyRange;
        
      //If fence is null do nothing
        if(GlobalApplication.allGeofencesStore == null){
            return;
        }
        else {
            //Get the list of IDs
           //List<String> ids = GlobalApplication.allGeofenceIDs;
        	AlertSummaryAdapter alerts = GlobalApplication.adapter;

            //Create iterator to traverse the Geofences
            //Iterator<String> iterator = ids.iterator();
            int count = alerts.getCount();
        	
            //while (iterator.hasNext()) {
        	for(int i=0;i<count;i++){
        		
                //Get the geofence with the id
        		AlertSummary item = alerts.getItem(i);
        		String url = item.getUrl();
                SimpleGeofence fence = GlobalApplication.allGeofencesStore.getGeofence(url);

                //Set the LatLng
                LatLng fenceLatLng = new LatLng(fence.getLatitude(),fence.getLongitude());
                
                //Calculate distance
                double dist = com.google.maps.android.SphericalUtil.computeDistanceBetween(fenceLatLng, User);              
                
                if(dist <= range){           
                
	                Marker alert = map.addMarker(new MarkerOptions().position(fenceLatLng)
	                        .title(fence.getId())
	                        .snippet(fence.getId()));
	
	                //Draw circles for each geofence
	                Circle circle = map.addCircle(new CircleOptions()
	                        .center(fenceLatLng)
	                        .radius(fence.getRadius())
	                        .strokeColor(Color.RED)
	                        .strokeWidth(5)
	                        .fillColor(0x40ff0000)   //Semi-transparent
	                );
                }
            }
        }
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}

