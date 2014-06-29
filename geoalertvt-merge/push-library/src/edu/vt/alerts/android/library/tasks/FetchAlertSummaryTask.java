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
package edu.vt.alerts.android.library.tasks;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;
import edu.vt.alerts.android.library.callbacks.AlertSummaryCallback;
import edu.vt.alerts.android.library.exceptions.UnexpectedNetworkResponseException;
import edu.vt.alerts.android.library.util.HttpClientFactory;
import edu.vt.alerts.android.library.util.marshal.JsonAlertSummaryUnmarshaller;
import edu.vt.alerts.android.library.util.marshal.Unmarshaller;

/**
 * AsyncTask that is used to fetch the summaries for alerts from the VT-APNS
 * system.
 *
 * @author Michael Irwin
 */
public class FetchAlertSummaryTask 
    extends AsyncTask<Date, Void, TaskResult<List<AlertSummary>>>{
  
	private final Context context;
	private final Environment environment;
	private final AlertSummaryCallback callback;
	
	private Unmarshaller<List<AlertSummary>> unmarshaller = 
	    new JsonAlertSummaryUnmarshaller();
	private HttpClientFactory httpClientFactory = new HttpClientFactory();
	
	/**
	 * Create a new instance
	 * @param context The application context
	 * @param environment The alerts environment to run with
	 * @param callback A callback to notify of results
	 */
	public FetchAlertSummaryTask(Context context, Environment environment,
	    AlertSummaryCallback callback) {
		this.context = context;
		this.environment = environment;
		this.callback = callback;
	}
	
	@Override
	protected TaskResult<List<AlertSummary>> doInBackground(Date... params) {
		try {
			HttpClient client = 
			    httpClientFactory.generateSubscriberClient(context, environment);
			String suffix = (params.length > 0) ? 
			    "?timestamp=" + params[0].getTime() / 1000 : "";
			HttpGet get = new HttpGet(environment.getAlertsSummaryUrl() + suffix);
			get.addHeader("Accept", unmarshaller.getMimeType());
			
			HttpResponse response = client.execute(get);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode < 200 || statusCode >= 300)
				throw new UnexpectedNetworkResponseException(
				    "Unexpected response (" + statusCode + ") when fetching summaries",
				    statusCode, statusLine.getReasonPhrase());
			
			String alertSummary = new BasicResponseHandler().handleResponse(response);
			Log.i("alertsService", "Retrieved the following:");
			Log.i("alertsService", alertSummary);
			List<AlertSummary> alerts = unmarshaller.convertData(alertSummary);
			return new TaskResult<List<AlertSummary>>(alerts, null);
		} catch (Exception e) {
			return new TaskResult<List<AlertSummary>>(null, e);
		}
	}
	
	@Override
	protected void onPostExecute(TaskResult<List<AlertSummary>> result) {
		if (result.exceptionThrown())
			callback.exceptionThrown(result.getException());
		else
			callback.alertsRetrieved(result.getResult());
	}
	
}
