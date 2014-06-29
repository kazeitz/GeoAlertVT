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
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.callbacks.RegistrationCallback;
import edu.vt.alerts.android.library.callbacks.TermsOfServiceCallback;
import edu.vt.alerts.android.library.exceptions.UnexpectedNetworkResponseException;
import edu.vt.alerts.android.library.util.PreferenceUtil;

/**
 * An AsyncTask that accomplishes the following tasks:
 * 	1) Determines if the Terms of Service need to be accepted (have they already
 *     been accepted?)
 *  2) If they need to be accepted, fetches the terms of service from the VT
 *     Alerts Push Notification System content service. Then, notifies the
 *     RegistrationCallback that the Terms of Service need to be accepted.
 *  3) If they do not need to be accepted, the TermsOfServiceCallback is used to
 *     inform the callback that the terms have been accepted.
 *     
 * The TaskResult is used as follows:
 *   - If an exception is thrown, the exception is captured
 *   - If a String result exists, it means that the Terms of Service are 
 *     required to be shown to the user
 *   - If no String result exists (is null), the Terms of Service have already 
 *     been accepted.
 * 
 * @author Michael Irwin
 */
public class TermsOfServiceTask extends AsyncTask<Void, Void, TaskResult<String>> {

	final private Context context;
	final private Environment alertsEnvironment;
	final private RegistrationCallback registrationCallback;
	final private TermsOfServiceCallback termsOfServiceCallback;

	/**
	 * Create a new instance of the task
	 * @param context The application context
	 * @param environment The environment to be used for the terms of service
	 * @param registrationCallback The registration callback
	 * @param termsOfServiceCallback A callback to be used if the terms of service
	 *        need to be accepted by the user.
	 */
	public TermsOfServiceTask(Context context, Environment environment,
			RegistrationCallback registrationCallback,
			TermsOfServiceCallback termsOfServiceCallback) {
		this.context = context;
		this.registrationCallback = registrationCallback;
		this.termsOfServiceCallback = termsOfServiceCallback;
		this.alertsEnvironment = environment;
	}

  /**
   * {@inheritDoc}
   */
	@Override
	protected TaskResult<String> doInBackground(Void... params) {
		if (PreferenceUtil.hasAcceptedTerms(context, alertsEnvironment)) {
			return new TaskResult<String>(null, null);
		}
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(alertsEnvironment.getTermsOfUseUrl());
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
      HttpResponse response = httpClient.execute(get);
      int statusCode = response.getStatusLine().getStatusCode();
      
      if (statusCode >= 200 && statusCode < 300) {
        String tos = responseHandler.handleResponse(response);
      	return new TaskResult<String>(tos, null);
      }
      
      throw new UnexpectedNetworkResponseException(
          "Unexpected response when attempting to fetch terms of service",
          statusCode, response.getStatusLine().getReasonPhrase());
		} catch (Exception e) {
		  return new TaskResult<String>(null, e);
		}
	}

  /**
   * {@inheritDoc}
   */
	@Override
	protected void onPostExecute(TaskResult<String> result) {
		if (result.exceptionThrown()) {
			registrationCallback.exceptionThrown(result.getException());
		}
		
		String termsOfUse = result.getResult();
		if (termsOfUse != null && termsOfUse.length() > 0) {
			registrationCallback.displayTermsOfService(termsOfUse,
					termsOfServiceCallback);
		}
		else {
			termsOfServiceCallback.termsAccepted();
		}
	}
	
}
