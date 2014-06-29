/*
 * File created on Feb 28, 2014 
 *
 * Copyright 2008-2014 Virginia Polytechnic Institute and State University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package edu.vt.alerts.android.library.demo;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.vt.alerts.android.library.api.AlertService;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;
import edu.vt.alerts.android.library.callbacks.AlertCallback;
import edu.vt.alerts.android.library.domain.cap.CapAlert;
import edu.vt.alerts.android.library.domain.cap.CapArea;
import edu.vt.alerts.android.library.domain.cap.CapInfo;

/**
 * Activity to display the details for a specific alert
 *
 * @author Michael Irwin
 * @author Kimberly Zeitz
 */
public class AlertActivity extends BaseActivity implements AlertCallback, OnClickListener {

  public static final String ALERT_EXTRA = "alert";
  
  private AlertService alertService;
  
  @InjectView(R.id.headline)          TextView headline;
  @InjectView(R.id.description)       TextView description;
  @InjectView(R.id.sent)              TextView sent;
  @InjectView(R.id.expires)           TextView expires;
  @InjectView(R.id.event)             TextView event;
  @InjectView(R.id.label_location)    TextView locationLabel;
  @InjectView(R.id.location)          TextView location;
  
  Button mapButton;
  LatLng center;
  Float radius;
  AlertSummary summary;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alert);
    
    //Setting up for the map view button listener
    mapButton = (Button) findViewById(R.id.map_button);
    mapButton.setOnClickListener(this);

    //TODO Broadcast Receiver
    
    ButterKnife.inject(this);
    
    alertService = new AlertService(Environment.SANDBOX);
    
    if (!getIntent().hasExtra(ALERT_EXTRA)) {
      Toast.makeText(this, "An alert wasn't provided", Toast.LENGTH_SHORT)
          .show();
      finish();
    }
    
    summary = 
        (AlertSummary) getIntent().getSerializableExtra(ALERT_EXTRA);
    displayDialog("Fetching alert details...");
    alertService.retrieveAlert(this, summary.getUrl(), this);
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Intent getParentActivityIntent() {
    finish();
    return super.getParentActivityIntent();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void alertRetrieved(CapAlert alert) {
    checkDialog();
    if (alert == null) {
      Log.i("alert", "Got a null alert??");
    }
    
    CapInfo info = alert.getInfo().get(0);
    headline.setText(info.getHeadline());
    description.setText(info.getDescription());
    event.setText(info.getEvent());
    expires.setText(info.getExpires().toString());
    sent.setText(alert.getSent().toString());
    
    String locationText = "[Unspecified]";
   // if (info.getArea().size() > 0) {
      locationLabel.setVisibility(View.VISIBLE);
      location.setVisibility(View.VISIBLE);
      
      //Get area
      CapArea area = info.getArea().get(0);
      
      //Separate out coordinates, radius, and other text
      String circ = area.getCircle().get(0);
      List<String> areaFields = Arrays.asList(circ.split(" "));
      
      //Set the center location
      String latLngString = areaFields.get(0); 
      List<String> latLng = Arrays.asList(latLngString.split(","));
      String latString = latLng.get(0);
      String lngString = latLng.get(1);
      
      //remove trailing comma
      if (latString.endsWith(",")) {
    	  latString = latString.substring(0, latString.length() - 1);
    	}
      
      double lat = Double.valueOf(latString);
      double lng = Double.valueOf(lngString);
      center = new LatLng(lat,lng);
      
      //Set the radius
      String radiusString = areaFields.get(1);
      radius = Float.valueOf(radiusString);
      
      //Set location display text
      String locationString ="";
      
      if(areaFields.size() > 2){
    	  for(int i = 2; i < areaFields.size(); i++){
    		  if(i == 2){
    			  locationString = locationString.concat(areaFields.get(i));
    		  }
    		  else{
    			  locationString = locationString.concat(" ");
    			  locationString = locationString.concat(areaFields.get(i));
    		  }
    	  }
    	  location.setText(locationString);
      }
      
     
      if (area.getCircle().size() > 0) {
        locationText = "Circle: " + area.getCircle().get(0);
        
      //Add Geofence
      long expirationHours = info.getExpires().getHours();
      GeofenceActivity geoActivity = new GeofenceActivity(); 
      String url = summary.getUrl();
      geoActivity.createGeofence(expirationHours, lat, lng, radius, url, this);
        
    //  } Debugging
  /*    int location = 0;
      Context context = getApplicationContext();
      String cir = area.getCircle().get(0);
      int size = info.getArea().size();
      int duration = Toast.LENGTH_LONG;

      Toast toast = Toast.makeText(context,i, duration);
      toast.show();*/
      
    }
      
  }
  
  private LatLng getAlertLatLng(){
	  return center;
  }
  
  private float getAlertRadius(){
	  return radius;
  }
  
  private void buttonMapClick(){
      Intent intent = new Intent(AlertActivity.this,MapActivity.class);
      startActivity(intent);
  }
  
  public void onClick(View view) {
          switch (view.getId()){
              case R.id.map_button:
                  buttonMapClick();
                  break;
          }
  }
  
}
