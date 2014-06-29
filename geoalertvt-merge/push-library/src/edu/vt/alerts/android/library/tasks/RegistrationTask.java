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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSSignedDataParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.vt.alerts.android.library.api.Environment;
import edu.vt.alerts.android.library.callbacks.RegistrationCallback;
import edu.vt.alerts.android.library.exceptions.UnexpectedNetworkResponseException;
import edu.vt.alerts.android.library.util.FileBasedKeyStoreContainer;
import edu.vt.alerts.android.library.util.GCMTokenObtainer;
import edu.vt.alerts.android.library.util.HttpClientFactory;
import edu.vt.alerts.android.library.util.KeyStoreContainer;
import edu.vt.alerts.android.library.util.PreferenceUtil;

/**
 * An AsyncTask that registers the current device with the VT Alerts Push
 * Notification System.
 * 
 * During registration, the following is performed on the background process:
 *   1) A KeyPair is created for the device.
 *   2) A CSR is generated using the generated KeyPair.
 *   3) The CSR is submitted to the VT Alerts Push Notification Registration
 *      system.
 *   4) A signed certificate is returned and stored in the KeyStoreContainer.
 *   5) Also in the response is a URL unique to the subscriber. This is also
 *      stored into preferences.
 *      
 * If any exception or unexpected result from the network occurs, the provided
 * RegistrationCallback's exceptionThrown method is called, providing the thrown
 * exception.
 * 
 * @author Michael Irwin
 */
public class RegistrationTask 
    extends AsyncTask<Void, Void, TaskResult<Boolean>> {

	private static final String CONTENT_TYPE = "application/pkcs10";
	private static final String ACCEPT_TYPE = "application/pkcs7-mime";
	private static final String KEYPAIR_ALGORITHM = "RSA";
	private static final Integer KEYPAIR_KEYSIZE = 2048;
	private static final String CSR_SIGNER_ALGORITHM = "SHA1withRSA";
	private static final String CSR_SIGNER_PROVIDER = "BC";

	private Context context;
	private String gcmSenderId;
	private Environment alertsEnvironment;
	private InputStream installerKeystore;
	private RegistrationCallback registrationCallback;
	private GCMTokenObtainer gcmTokenObtainer = new GCMTokenObtainer();
	private HttpClientFactory httpClientFactory = new HttpClientFactory();
	private KeyStoreContainer keyStoreContainer = new FileBasedKeyStoreContainer();

	/**
	 * Creates a new instance of the task
	 * @param alertsEnvironment The environment to run in
	 * @param registrationCallback The callback to relay results to
	 * @param gcmSenderId The sender id to use for GCM communication
	 * @param installerKeystore InputStream to the installer certificate keystore
	 * @param context The application context
	 */
	public RegistrationTask(Environment alertsEnvironment,
	    RegistrationCallback registrationCallback, String gcmSenderId, 
	    InputStream installerKeystore, Context context) {
	  this.alertsEnvironment = alertsEnvironment;
		this.registrationCallback = registrationCallback;
		this.gcmSenderId = gcmSenderId;
		this.context = context;
		this.installerKeystore = installerKeystore;
	}
	
  /**
   * {@inheritDoc}
   */
	@Override
	protected TaskResult<Boolean> doInBackground(Void... params) {
	  if (PreferenceUtil.getSubscriberUrl(context, alertsEnvironment) != null)
	    return new TaskResult<Boolean>(null, null);
	  
		try {
		  String gcmToken = gcmTokenObtainer.obtainToken(context, gcmSenderId);
		  Log.d("registrationService", "Got GCM token: " + gcmToken);
		  
			KeyPair keyPair = generateKeyPair();
			Log.d("registrationService", "keyPair has been generated");
			
			PKCS10CertificationRequest csr = generateCSR(keyPair);
			Log.d("registrationService", "csr has been generated");
			
			HttpClient httpClient = 
			    httpClientFactory.generateInstallerClient(context, installerKeystore);
			HttpPost post = new HttpPost(alertsEnvironment.getRegisterUrl() 
			    + "?token=" + gcmToken);
			post.setEntity(new ByteArrayEntity(csr.getEncoded()));
		    post.addHeader("Content-Type", CONTENT_TYPE);
		    post.addHeader("Accept", ACCEPT_TYPE);
		    
      Log.d("registrationService", "Sending httpPost of Content-Type " 
        + CONTENT_TYPE + " to " + post.getURI());
		    
			HttpResponse response = httpClient.execute(post);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.d("registrationService", "Got a " + 
					statusLine.getStatusCode() + " back");
			if (statusCode < 200 || statusCode >= 300) {
				throw new UnexpectedNetworkResponseException("Unexpected response (" 
			      + statusCode + ") while trying to post certificate", statusCode, 
						statusLine.getReasonPhrase());
			}
			
			KeyStore keyStore = createKeyStore(keyPair, response);
			keyStoreContainer.storeKeyStore(context, alertsEnvironment, keyStore);
      String location = response.getLastHeader("Location").getValue();
			PreferenceUtil.setSubscriberUrl(context, alertsEnvironment, location);
		} catch (Exception e) {
		  Log.e("registration", "An exception has occurred during registration", e);
			return new TaskResult<Boolean>(false, e);
		}
		return new TaskResult<Boolean>(true, null);
	}
	
  /**
   * {@inheritDoc}
   */
	@Override
	protected void onPostExecute(TaskResult<Boolean> result) {
		if (result.exceptionThrown()) {
			registrationCallback.exceptionThrown(result.getException());
		} else if (result.getResult() == null) {
		  registrationCallback.alreadyRegistered();
		} else {
			registrationCallback.registrationSuccessful();
		}
	}
	
	private KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM);
		kpg.initialize(KEYPAIR_KEYSIZE);
		return kpg.generateKeyPair();
	}
	
	private PKCS10CertificationRequest generateCSR(KeyPair keyPair) 
	    throws Exception {
		JcaPKCS10CertificationRequestBuilder builder = 
				new JcaPKCS10CertificationRequestBuilder(
						new X500Name("CN=edu.vt.alerts.mobile.android"), 
						keyPair.getPublic());
		
		ContentSigner signer = new JcaContentSignerBuilder(CSR_SIGNER_ALGORITHM)
        	.setProvider(CSR_SIGNER_PROVIDER).build(keyPair.getPrivate());
		
		return builder.build(signer);
	}
	
	private KeyStore createKeyStore(KeyPair keyPair, HttpResponse response) 
	    throws Exception {
	  Log.i("registration", "Got status from registration server: " 
	    + response.getStatusLine());
	    
		HttpEntity entity = response.getEntity();
  	byte[] contents = getBytes(entity.getContent());
  	Collection<?> certs = extractCerts(contents);
    Certificate[] certificates = new Certificate[certs.size()];
    Log.i("registration", "Extracted out " + certs.size() + " certs");
    
    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
    Iterator<?> it = certs.iterator();
    int i = 0;
    while (it.hasNext()) {
      byte[] encoded = ((X509CertificateHolder) it.next()).getEncoded();
      certificates[i++] = (X509Certificate) 
          certFactory.generateCertificate(new ByteArrayInputStream(encoded));
    }
    
    Log.d("registration", "Creating local keystore");
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setKeyEntry("Cert", keyPair.getPrivate(), 
        "changeit".toCharArray(),certificates);
    
    return keyStore;
	}
	
	private byte[] getBytes(InputStream is) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		byte[] bytes = buffer.toByteArray();
		Log.i("registration", "Have " + bytes.length + " bytes");
		return bytes;
	}
	
	private Collection<?> extractCerts(byte[] contents) throws Exception {
    JcaDigestCalculatorProviderBuilder builder = 
        new JcaDigestCalculatorProviderBuilder();
    builder.setProvider(CSR_SIGNER_PROVIDER);
    DigestCalculatorProvider provider = builder.build();
    CMSSignedDataParser parser = new CMSSignedDataParser(
        provider, contents);
    Store store = parser.getCertificates();
    return store.getMatches(certSelector);
	}
	
	private Selector certSelector = new Selector() {
  	public Object clone() {
  		return this;
  	}
  	public boolean match(Object arg0) {
  		return true;
  	}
  };
  
}
