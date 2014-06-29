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
import org.apache.http.client.methods.HttpDelete;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.callbacks.CancelRegistrationCallback;
import edu.vt.alerts.android.library.exceptions.UnexpectedNetworkResponseException;
import edu.vt.alerts.android.library.util.FileBasedKeyStoreContainer;
import edu.vt.alerts.android.library.util.HttpClientFactory;
import edu.vt.alerts.android.library.util.KeyStoreContainer;
import edu.vt.alerts.android.library.util.PreferenceUtil;

/**
 * AsyncTask that is used to revoke or cancel a device's registration with the 
 * VT Alerts Push Notification System.
 * 
 * @author Michael Irwin
 */
public class CancelRegistrationTask 
    extends AsyncTask<Void, Void, TaskResult<Boolean>> {

	private final Context context;
	private final Environment environment;
	private final CancelRegistrationCallback callback;
	private HttpClientFactory httpClientFactory = new HttpClientFactory();
	private KeyStoreContainer keyStoreContainer = new FileBasedKeyStoreContainer();
	
	/**
	 * Create a new instance of the task
	 * @param context The application context
	 * @param environment The environment to use
	 * @param callback A callback to notify of results
	 */
	public CancelRegistrationTask(Context context, Environment environment,
	    CancelRegistrationCallback callback) {
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
				return new TaskResult<Boolean>(true, null);

			String subscriberUrl = 
			    PreferenceUtil.getSubscriberUrl(context, environment);
			if (subscriberUrl == null || subscriberUrl.length() == 0)
				return new TaskResult<Boolean>(true, null);
			
			HttpClient client =
			    httpClientFactory.generateSubscriberClient(context, environment);
			HttpDelete delete = new HttpDelete(subscriberUrl);
			HttpResponse response = client.execute(delete);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.d("cancelRegistration", "Received a " + statusCode);
			
			if (statusCode >= 200 || statusCode < 300) {
				return new TaskResult<Boolean>(true, null);
			} else if (statusCode == 401 || statusCode == 403 || statusCode == 404) {
				return new TaskResult<Boolean>(true, null);
			}
			
			throw new UnexpectedNetworkResponseException(
			    "Unexpected response while trying to cancel registration", statusCode, 
					statusLine.getReasonPhrase());
		} catch (Exception e) {
			return new TaskResult<Boolean>(false, e);
		}
	}
	
  /**
   * {@inheritDoc}
   */
	@Override
	protected void onPostExecute(TaskResult<Boolean> result) {
		if (result.exceptionThrown()) {
			callback.exceptionThrown(result.getException());
			return;
		}
		Log.d("cancelRegistration", "Removing keystore and subscriber url");
		keyStoreContainer.removeKeyStore(context, environment);
		PreferenceUtil.setAcceptedTerms(context, environment, false);
		PreferenceUtil.setSubscriberUrl(context, environment, null);
		callback.registrationCancelled();
	}
	
}
