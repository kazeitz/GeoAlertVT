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

import android.content.Context;

import com.google.android.gcm.GCMRegistrar;

/**
 * An implementation of the GCMTokenObtainer that does the following:
 *   1) Check if already registered.  If so, return the token.
 *   2) If not, registers the device.
 *   3) Goes into a loop to check if the gcm id has been set.  If not, it waits
 *      one second and tries again.
 * 
 * @author Michael Irwin
 */
public class GCMTokenObtainer {
	
	/**
	 * Obtain a GCM token for the current client using the provided sender id
	 * @param context The application context
	 * @param gcmSenderId The GCM sender id (from Google console)
	 * @return The GCM token.
	 */
	public String obtainToken(Context context, String gcmSenderId) {
	  GCMRegistrar.checkManifest(context);
	  
		String gcmToken = GCMRegistrar.getRegistrationId(context);
		if (gcmToken != null && gcmToken.length() > 0) {
			return gcmToken;
		}
		
		GCMRegistrar.register(context, gcmSenderId);
		
		int counter = 0;
		while (gcmToken == null || gcmToken.length() == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			gcmToken = GCMRegistrar.getRegistrationId(context);
			if (counter++ == 5)
			  throw new RuntimeException("Unable to obtain GCM token");
		}
		
		return gcmToken;
	}
	
}
