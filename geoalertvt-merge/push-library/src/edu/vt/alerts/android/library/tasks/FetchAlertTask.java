package edu.vt.alerts.android.library.tasks;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.callbacks.AlertCallback;
import edu.vt.alerts.android.library.domain.cap.CapAlert;
import edu.vt.alerts.android.library.exceptions.UnexpectedNetworkResponseException;
import edu.vt.alerts.android.library.util.HttpClientFactory;
import edu.vt.alerts.android.library.util.marshal.JsonAlertUnmarshaller;
import edu.vt.alerts.android.library.util.marshal.Unmarshaller;

/**
 * An AsyncTask that fetches the individual details for Alerts.
 * 
 * The AsyncTask supports execution for multiple alerts.  On firing execute,
 * the neccessary parameter is the AlertSummary.url for the alerts wishing to
 * be retrieved.
 * 
 * @author Michael Irwin
 *
 */
public class FetchAlertTask extends AsyncTask<String, CapAlert, TaskResult<Boolean>>{

	private final Context context;
	private final Environment environment;
	private final AlertCallback callback;
	
	private HttpClientFactory httpClientFactory = new HttpClientFactory();
	private Unmarshaller<CapAlert> unmarshaller = new JsonAlertUnmarshaller();
	
	/**
	 * Create a new instance
	 * @param context The application context
	 * @param environment The alerts environment to run in
	 * @param callback A callback to notify or results
	 */
	public FetchAlertTask(Context context, Environment environment,
	    AlertCallback callback) {
		this.context = context;
		this.environment = environment;
		this.callback = callback;
	}
	
	@Override
	protected TaskResult<Boolean> doInBackground(String... params) {
		try {
			HttpClient client = 
			    httpClientFactory.generateSubscriberClient(context, environment);
			for (int i = 0; i < params.length; i++) {
				HttpGet get = 
				    new HttpGet(environment.getAlertRetrievalBaseUrl() + params[i]);
				get.addHeader("Accept", unmarshaller.getMimeType());
				
				HttpResponse response = client.execute(get);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode < 200 || statusCode >= 300)
					throw new UnexpectedNetworkResponseException("Unexpected response (" 
				      + statusCode + ") while fetching alert: " + params[i],
					    statusCode, statusLine.getReasonPhrase());
				
				String alertData = new BasicResponseHandler().handleResponse(response);
				Log.d("alertService", "Retrieved the following:");
				Log.d("alertService", alertData);
				CapAlert alert = unmarshaller.convertData(alertData);
				publishProgress(alert);
			}
		} catch (Exception e) {
			return new TaskResult<Boolean>(false, e);
		}
		return new TaskResult<Boolean>(true, null);
	}
	
	@Override
	protected void onProgressUpdate(CapAlert... values) {
		super.onProgressUpdate(values);
		CapAlert alert = values[0];
		callback.alertRetrieved(alert);
	}
	
	@Override
	protected void onPostExecute(TaskResult<Boolean> result) {
		if (result.exceptionThrown())
			callback.exceptionThrown(result.getException());
	}

}
