/*
 * File created on Feb 24, 2014 
 *
 * Copyright 2008-2013 Virginia Polytechnic Institute and State University
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
package edu.vt.alerts.android.library.api;

import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.vt.alerts.android.library.callbacks.CancelRegistrationCallback;
import edu.vt.alerts.android.library.callbacks.RegistrationCallback;
import edu.vt.alerts.android.library.callbacks.TermsOfServiceCallback;
import edu.vt.alerts.android.library.callbacks.VerifyCallback;
import edu.vt.alerts.android.library.tasks.CancelRegistrationTask;
import edu.vt.alerts.android.library.tasks.RegistrationTask;
import edu.vt.alerts.android.library.tasks.TermsOfServiceTask;
import edu.vt.alerts.android.library.tasks.VerificationTask;
import edu.vt.alerts.android.library.util.PreferenceUtil;

/**
 * An Alerts service that is used to register users to the VT Alerts Push
 * Notification System.
 * 
 * All method calls to this service are supported using AsyncTasks.  Therefore,
 * methods will return almost immediately.  Callbacks are then used to 
 * communicate results back to the calling code.  It is safe to assume that all
 * method callbacks occur on the UI thread, rather than the background thread
 * for the AsyncTask.
 *
 * @author Michael Irwin
 */
public class RegistrationService {

  private final Environment environment;
  
  /**
   * Create a new instance of the service. By default, it uses the PRODUCTION
   * environment.
   */
  public RegistrationService() {
    this(Environment.PRODUCTION);
  }
  
  /**
   * Creates a new instance of the service that is configured for the specified
   * environment.
   * @param environment The environment to use for the service.
   */
  public RegistrationService(Environment environment) {
    this.environment = environment;
  }
  
  /**
   * Start the registration process for the current device.  The provided 
   * callback is used to communicate results of the registration process
   * @param context The application context
   * @param gcmSenderId The GCM sender ID for the application
   * @param installerCert An inputStream for the installer's certificate
   *        provided during VT-APNS application registration
   * @param callback A callback used to communicate registration
   *        results.
   */
  public void performRegistration(final Context context, 
      final String gcmSenderId, final InputStream installerCert, 
      final RegistrationCallback callback) {
    TermsOfServiceTask tosTask = new TermsOfServiceTask(context, environment, 
        callback, new TermsOfServiceCallback() {

          public void termsAccepted() {
            PreferenceUtil.setAcceptedTerms(context, environment, true);
            Log.i("registrationService", "Terms of service were accepted");
            RegistrationTask registrationTask = new RegistrationTask(
                environment, callback, gcmSenderId, installerCert, context);
            registrationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
                new Void[0]);
          }

          public void termsRejected() {
            PreferenceUtil.setAcceptedTerms(context, environment, false);
            Log.i("registrationService", "Terms of service were rejected");
            callback.registrationAborted();
          }
          
        });
    tosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
  }
  
  /**
   * Cancel the subscription for the current device in the configured 
   * environment.
   * @param context The application context
   * @param callback A callback to be used to notify of completion
   */
  public void cancelRegistration(final Context context, 
      final CancelRegistrationCallback callback) {
    CancelRegistrationTask task = 
        new CancelRegistrationTask(context, environment, callback);
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
  }
  
  /**
   * Verify the current device's subscription to the VT-APNS system.
   * @param context The application context
   * @param callback A callback to notify of verification results
   */
  public void verifyRegistration(final Context context, 
      final VerifyCallback callback) {
    VerificationTask task = 
        new VerificationTask(context, environment, callback);
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
  }
  
}

