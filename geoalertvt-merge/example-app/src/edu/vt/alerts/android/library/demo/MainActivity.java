/*
 * File created on Feb 25, 2014 
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

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import edu.vt.alerts.android.library.api.AlertService;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.api.RegistrationService;
import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;
import edu.vt.alerts.android.library.api.domain.summary.Info;
import edu.vt.alerts.android.library.callbacks.AlertSummaryCallback;
import edu.vt.alerts.android.library.callbacks.CancelRegistrationCallback;
import edu.vt.alerts.android.library.callbacks.RegistrationCallback;
import edu.vt.alerts.android.library.callbacks.TermsOfServiceCallback;
import edu.vt.alerts.android.library.callbacks.VerifyCallback;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.SimpleGeofenceStore;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.GeofenceUtils.REMOVE_TYPE;
import edu.vt.alerts.android.library.demo.AndroidGeoFence.GeofenceUtils.REQUEST_TYPE;
import edu.vt.alerts.android.library.demo.adapters.AlertSummaryAdapter;
import edu.vt.alerts.android.library.domain.cap.CapAlert;
import edu.vt.alerts.android.library.domain.cap.CapArea;
import edu.vt.alerts.android.library.domain.cap.CapInfo;

/**
 * The main activity. Nothing too exciting here.
 * 
 * @author Michael Irwin
 */
public class MainActivity extends BaseActivity implements VerifyCallback, 
    RegistrationCallback, CancelRegistrationCallback,
    AlertSummaryCallback {
  
  private static final String GCM_SENDER_ID = "34777061016";
  
  private RegistrationService registrationService;
  private AlertService alertService;
  
  @InjectView(R.id.indicator_verifying)          View mVerifyingView;
  @InjectView(R.id.indicator_connected)          View mConnectedView;
  @InjectView(R.id.indicator_disconnected)       View mDisconnectedView;
  @InjectView(R.id.button_register)              Button mRegisterButton;
  @InjectView(R.id.button_verify)                Button mVerifyButton;
  @InjectView(R.id.button_cancel)                Button mCancelButton;
  @InjectView(R.id.button_fetch_alerts)          Button mFetchAlertsButton;
  @InjectView(R.id.alert_summaries_placeholder)  TextView mSummariesPlaceholder;
  @InjectView(R.id.alert_list_view)              ListView mAlertListView;
  

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    GCMRegistrar.checkManifest(this);
    
    mConnectedView.setVisibility(View.GONE);
    mDisconnectedView.setVisibility(View.GONE);
    
    registrationService = new RegistrationService(Environment.SANDBOX);
    registrationService.verifyRegistration(this, this);
    
    alertService = new AlertService(Environment.SANDBOX);
    
    //Fetch alerts as soon as opens
    alertService.retreiveAlertSummaries(this, this);

  }

  @OnClick(R.id.button_register)
  public void onRegisterButtonClick() {
    displayDialog("Registering...");
    InputStream installerCert = 
        getResources().openRawResource(R.raw.installer);
    registrationService.performRegistration(this, GCM_SENDER_ID, 
        installerCert, this);
  }
  
  @OnClick(R.id.button_verify)
  public void onVerifyButtonClick() {
    displayDialog("Verifying connection...");
    registrationService.verifyRegistration(this, this);
  }
  
  @OnClick(R.id.button_cancel)
  public void onCancelButtonClick() {
    displayDialog("Cancelling...");
    registrationService.cancelRegistration(this, this);
  }
  
  @OnClick(R.id.button_fetch_alerts)
  public void onFetchAlertsButtonClick() {
    displayDialog("Fetching alert summaries...");
    alertService.retreiveAlertSummaries(this, this);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void deviceNotRegistered() {
    checkDialog();
    Log.i("demo", "=== Not registered");
    mVerifyingView.setVisibility(View.GONE);
    mConnectedView.setVisibility(View.GONE);
    mDisconnectedView.setVisibility(View.VISIBLE);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void deviceVerified() {
    checkDialog();
    Log.i("demo", "=== Is registered");
    mVerifyingView.setVisibility(View.GONE);
    mConnectedView.setVisibility(View.VISIBLE);
    mDisconnectedView.setVisibility(View.GONE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayTermsOfService(String termsOfService,
      final TermsOfServiceCallback callback) {
    checkDialog();
    
    Log.i("demo", "Time to display the terms of service!");
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle("Terms of Service");
    alertDialogBuilder
      .setMessage(Html.fromHtml(termsOfService))
      .setCancelable(false)
      .setPositiveButton("Accept",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          callback.termsAccepted();
          dialog.dismiss();
          displayDialog("Registering...");
        }
      })
      .setNegativeButton("Reject",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          callback.termsRejected();
          dialog.dismiss();
        }
      });
 
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void alreadyRegistered() {
    checkDialog();
    Log.i("demo", "Looks like you're already registered!");
    Toast.makeText(this, "Looks like you're already registered", 
        Toast.LENGTH_SHORT).show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registrationAborted() {
    checkDialog();
    Log.i("demo", "Uh oh! Registration was aborted!");
    Toast.makeText(this, "Registration has been aborted", Toast.LENGTH_SHORT)
      .show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registrationSuccessful() {
    checkDialog();
    Log.i("demo", "Woot! Registration was successful!");
    Toast.makeText(this, "Registration was successful!", Toast.LENGTH_SHORT)
      .show();
    registrationService.verifyRegistration(this, this);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registrationCancelled() {
    checkDialog();
    Log.i("demo", "Registration has been removed!");
    Toast.makeText(this, "Registration has been cancelled", Toast.LENGTH_SHORT)
      .show();
    registrationService.verifyRegistration(this, this);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void alertsRetrieved(List<AlertSummary> alerts) {
    checkDialog();
    mSummariesPlaceholder.setVisibility(View.GONE);
    Toast.makeText(this, "Retrieved " + alerts.size() + " " + 
        ((alerts.size() == 1) ? "alert" : "alerts"), Toast.LENGTH_SHORT).show();
    GlobalApplication.adapter = new AlertSummaryAdapter(this, 
        alerts.toArray(new AlertSummary[0]));
    
    //Setup the geofences
    GeofenceActivity geoActivity = new GeofenceActivity(); 
    geoActivity.setUp(this);
   
    /*//Add Geofences
    for(int i = 0; i < alerts.size(); i++){
    	
    	//AlertSummary alert = adapter.getItem(i);
    	CapInfo info = adapter.
    	//CapInfo info =  alerts.get(i).getInfo().get(i);
    	//CapInfo info =  alerts.
		long expirationHours = info.getExpires().getTime();
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
	      LatLng center = new LatLng(lat,lng);
	      
	      //Set the radius
	      String radiusString = areaFields.get(1);
	      float radius = Float.valueOf(radiusString);
    	
    	GeofenceActivity geoActivity = new GeofenceActivity(); 
    	geoActivity.createGeofence(expirationHours, lat, lng, radius, String.valueOf(i));
    }*/
 
    mAlertListView.setAdapter(GlobalApplication.adapter);
    mAlertListView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> arg0, View view, int position,
          long arg3) {
        AlertSummary summary = GlobalApplication.adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, AlertActivity.class);
        intent.putExtra(AlertActivity.ALERT_EXTRA, summary);
        startActivity(intent);
      }
    });
    
  }

}
