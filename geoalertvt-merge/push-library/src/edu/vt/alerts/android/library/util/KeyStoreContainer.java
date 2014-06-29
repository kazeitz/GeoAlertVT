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

import java.security.KeyStore;

import edu.vt.alerts.android.library.api.Environment;
import android.content.Context;

/**
 * A KeyStoreContainer is used to help store and retrieve the credential that is
 * created during the registration process to represent the subscriber with the
 * VT Alerts Push Notification System.
 * 
 * @author Michael Irwin
 */
public interface KeyStoreContainer {

	/**
	 * Store the provided KeyStore into the container.
	 * @param context The application context.
	 * @param keyStore The KeyStore to save.
   * @param environment The VTAPNS environment
	 * @throws Exception Any exception that might be thrown.
	 */
	void storeKeyStore(Context context, Environment environment, 
	    KeyStore keyStore) throws Exception;
	
	/**
	 * Retrieve the subscriber's credential from its storage location.
	 * @param context The application context.
   * @param environment The VTAPNS environment
	 * @return The KeyStore that contains the subscriber's credential. Null if
	 * one doesn't exist.
	 * @throws Exception
	 */
	KeyStore retrieveKeyStore(Context context, Environment environment) 
	    throws Exception;
	
	/**
	 * Remove the subscriber's credential from its storage location.
	 * @param context The application context.
	 * @param environment The VTAPNS environment
	 */
	void removeKeyStore(Context context, Environment environment);
	
}
