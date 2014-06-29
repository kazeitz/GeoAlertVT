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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.UUID;

import android.content.Context;
import edu.vt.alerts.android.library.api.Environment;

/**
 * An implementation of the KeyStoreContainer that simply stores the 
 * subscriber's credential in the private file system for the application.
 * 
 * This container creates two entries in the preferences to store the 
 * certificate type and provider. These are created during storage of the
 * KeyStore and used when retrieving the KeyStore.
 * 
 * @author Michael Irwin
 */
public class FileBasedKeyStoreContainer implements KeyStoreContainer {

	private static final String KEYSTORE_FILENAME = "subscriber_keystore.p12";
	private static final String SUBSCRIBER_CERT_PROVIDER = "subscriber_cert_provider";
	private static final String SUBSCRIBER_CERT_TYPE = "subscriber_cert_type";
	private static final String SUBSCRIBER_CERT_PASS = "subscriber_cert_passphrase";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeyStore retrieveKeyStore(Context context, 
	    Environment env) throws Exception {
	  try {
	    FileInputStream fis = context.openFileInput(getFilename(env));
  		KeyStore keyStore = KeyStore.getInstance(
  				PreferenceUtil.getString(context, env, SUBSCRIBER_CERT_TYPE), 
  				PreferenceUtil.getString(context, env, SUBSCRIBER_CERT_PROVIDER));
  		keyStore.load(fis, 
  		    PreferenceUtil.getString(context, env, 
  		        SUBSCRIBER_CERT_PASS).toCharArray());
  		return keyStore;
	  } catch (FileNotFoundException e) {
	    return null;
	  }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeKeyStore(Context context, Environment env, 
	    KeyStore keyStore) throws Exception {
		String passPhrase = UUID.randomUUID().toString();
		
		FileOutputStream fos = context.openFileOutput(getFilename(env), 
		    Context.MODE_PRIVATE);
	  keyStore.store(fos, passPhrase.toCharArray());

		PreferenceUtil.putString(context, env, SUBSCRIBER_CERT_PROVIDER, 
		    keyStore.getProvider().getName());
		PreferenceUtil.putString(context, env, SUBSCRIBER_CERT_TYPE, 
		    keyStore.getType());
		PreferenceUtil.putString(context, env, SUBSCRIBER_CERT_PASS, passPhrase);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeKeyStore(Context context, Environment env) {
		context.deleteFile(getFilename(env));
		PreferenceUtil.putString(context, env, SUBSCRIBER_CERT_PROVIDER, null);
		PreferenceUtil.putString(context, env, SUBSCRIBER_CERT_TYPE, null);
		PreferenceUtil.putString(context, env, SUBSCRIBER_CERT_PASS, null);
	}
	
	private String getFilename(Environment env) {
	  return env.name() + "_" + KEYSTORE_FILENAME;
	}
}
