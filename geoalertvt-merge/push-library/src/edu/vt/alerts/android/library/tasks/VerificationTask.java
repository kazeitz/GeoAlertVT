/*
 * File created on Feb 24, 2014 
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

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.callbacks.VerifyCallback;
import edu.vt.alerts.android.library.exceptions.UnexpectedNetworkResponseException;
import edu.vt.alerts.android.library.util.FileBasedKeyStoreContainer;
import edu.vt.alerts.android.library.util.HttpClientFactory;
import edu.vt.alerts.android.library.util.KeyStoreContainer;

/**
 * AsyncTask that is used to verify that the provided device is registered with
 * the VT Alerts Push Notification System.
 * 
 * Verification is made by attempting to GET the subscriber's URL, presenting
 * the certificate for the subscriber.
 * 
 * The following outcomes can occur:
 *   1) If the subscriber has no certificate or URL, verification fails.
 *   2) If the remote server returns a 401/403/404, verification fails.
 *   3) If the remote server returns a 200, verification passes.
 *   4) Any other response throws a UnexpectedNetworkResultException and is
 *   	passed to the callback.
 *   
 * 
 * @author Michael Irwin
 */
public class VerificationTask extends AsyncTask<Void, Void, TaskResult<Boolean>> {

	private final Context context;
	private final VerifyCallback callback;
	private final Environment environment;
	private HttpClientFactory httpClientFactory = new HttpClientFactory();
	private KeyStoreContainer keyStoreContainer = 
	    new FileBasedKeyStoreContainer();
	
	/**
	 * Create a new instance of the task
	 * @param context The application context
	 * @param environment The environment to run in
	 * @param callback A callback to relay results
	 */
	public VerificationTask(Context context, Environment environment, 
	    VerifyCallback callback) {
		this.context = context;
		this.callback = callback;
		this.environment = environment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TaskResult<Boolean> doInBackground(Void... params) {
		try {
			if (keyStoreContainer.retrieveKeyStore(context, environment) == null)
				return new TaskResult<Boolean>(false, null);

			String subscriberUrl = environment.getRegisterUrl();
			if (subscriberUrl == null || subscriberUrl.length() == 0)
				return new TaskResult<Boolean>(false, null);
			
			HttpClient client = 
			    httpClientFactory.generateSubscriberClient(context, environment);
			HttpGet get = new HttpGet(subscriberUrl);
			HttpResponse response = client.execute(get);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.d("verification", "Received a " + statusCode);
			
			if (statusCode >= 200 || statusCode < 300) {
				return new TaskResult<Boolean>(true, null);
			} else if (statusCode == 401 || statusCode == 403 || statusCode == 404) {
				return new TaskResult<Boolean>(false, null);
			}
			
			throw new UnexpectedNetworkResponseException(
			    "Unexpected result while trying to verify subscription " + statusCode, 
			    statusCode, statusLine.getReasonPhrase());
		} catch (Exception e) {
			return new TaskResult<Boolean>(null, e);
		}
	}
	
  /**
   * {@inheritDoc}
   */
	@Override
	protected void onPostExecute(TaskResult<Boolean> result) {
		if (result.exceptionThrown()) {
			callback.exceptionThrown(result.getException());
		}
		else if (result.getResult()) {
			callback.deviceVerified();
		}
		else {
			callback.deviceNotRegistered();
		}
	}
	
}
