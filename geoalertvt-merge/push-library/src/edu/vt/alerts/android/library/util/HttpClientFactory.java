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
package edu.vt.alerts.android.library.util;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;
import edu.vt.alerts.android.library.R;
import edu.vt.alerts.android.library.api.Environment;

/**
 * An implementation of the HttpClientFactory that returns the HttpClient 
 * objects as expected.
 * 
 * @author Michael Irwin
 */
public class HttpClientFactory {
	
	private final KeyStoreContainer subscriberKeystoreContainer = 
	    new FileBasedKeyStoreContainer();

	/**
	 * Create an HttpClient that is configured with the installer certificate
	 * @param context The application context
	 * @param installerKeystore The installer certificate
	 * @return An HttpClient configured to talk to the VTAPNS using the supplied
	 * installer keystore
	 * @throws Exception Anything really
	 */
	public HttpClient generateInstallerClient(Context context, 
	    InputStream installerKeystore) throws Exception {
	  
		HttpParams httpParameters = new BasicHttpParams();

    SSLSocketFactory sockfact = new SSLSocketFactory(
        getInstallerKeyStore(context, installerKeystore), 
        "changeit", getTrustStore(context));
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(
        new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", sockfact, 443));
    
    return new DefaultHttpClient(
        new ThreadSafeClientConnManager(httpParameters, registry), 
        httpParameters);
	}
	
	/**
	 * Generate an HttpClient that is configured to use the subscriber's
	 * certificate
	 * @param context The application context
	 * @param env The environment to run in
	 * @return An HttpClient that is configured to talk to the VTAPNS using the
	 * subscriber's certificate
	 * @throws Exception Any exception really...
	 */
	public HttpClient generateSubscriberClient(Context context, 
	    Environment env) throws Exception {
		HttpParams httpParameters = new BasicHttpParams();

		KeyStore keyStore = 
		    subscriberKeystoreContainer.retrieveKeyStore(context, env);
    SSLSocketFactory sockfact = new SSLSocketFactory(keyStore, "changeit", 
    		getTrustStore(context));
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", 
        PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", sockfact, 443));

    return new DefaultHttpClient( 
    		new ThreadSafeClientConnManager(httpParameters, registry), 
    		httpParameters);
	}
	
	private KeyStore getTrustStore(Context context) throws Exception {
		KeyStore trustStore = KeyStore.getInstance("PKCS12");
		trustStore.load(context.getResources().openRawResource(R.raw.truststore), 
				"changeit".toCharArray());
	    return trustStore;
	}
	
	private KeyStore getInstallerKeyStore(Context context, 
	    InputStream installerKeystore) throws Exception {
		KeyStore installerKeyStore = KeyStore.getInstance("PKCS12");
	    installerKeyStore.load(installerKeystore, "changeit".toCharArray());
	    return installerKeyStore;
	}
	
}
