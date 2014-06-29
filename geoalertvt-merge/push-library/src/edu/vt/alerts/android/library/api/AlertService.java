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

import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import edu.vt.alerts.android.library.callbacks.AlertCallback;
import edu.vt.alerts.android.library.callbacks.AlertSummaryCallback;
import edu.vt.alerts.android.library.tasks.FetchAlertSummaryTask;
import edu.vt.alerts.android.library.tasks.FetchAlertTask;

/**
 * An Alerts service that is used to retrieve alerts data.
 * 
 * All method calls to this service are supported using AsyncTasks.  Therefore,
 * methods will return almost immediately.  Callbacks are then used to 
 * communicate results back to the calling code.  It is safe to assume that all
 * method callbacks occur on the UI thread, rather than the background thread
 * for the AsyncTask.
 *
 * @author Michael Irwin
 */
public class AlertService {

  private final Environment environment;
  
  /**
   * Create a new instance of the service. By default, it uses the PRODUCTION
   * environment.
   */
  public AlertService() {
    this(Environment.PRODUCTION);
  }
  
  /**
   * Creates a new instance of the service that is configured for the specified
   * environment.
   * @param environment The environment to use for the service.
   */
  public AlertService(Environment environment) {
    this.environment = environment;
  }
  
  /**
   * Retrieve a specific alert, by its url (supplied in the alert summary)
   * @param context The application context
   * @param url The url, found in the alert summary
   * @param callback A callback used to relay results
   */
  public void retrieveAlert(Context context, String url, 
      AlertCallback callback) {
    FetchAlertTask task = new FetchAlertTask(context, environment, callback);
    String[] params = { url };
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
  }
  
  /**
   * Request alert summaries, without specifying a start date
   * @param context The application context
   * @param callback A callback to be notified of the results
   */
  public void retreiveAlertSummaries(Context context, 
      AlertSummaryCallback callback) {
    FetchAlertSummaryTask task = new FetchAlertSummaryTask(context, 
        environment, callback);
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Date[0]);
  }
  
  /**
   * Request alert summaries, without specifying a start date
   * @param context The application context
   * @param sinceDate Date to fetch alerts since
   * @param callback A callback to be notified of the results
   */
  public void retreiveAlertSummaries(Context context, Date sinceDate,
      AlertSummaryCallback callback) {
    FetchAlertSummaryTask task = new FetchAlertSummaryTask(context, 
        environment, callback);
    
    Date[] dates = { sinceDate };
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dates);
  }
  
}

