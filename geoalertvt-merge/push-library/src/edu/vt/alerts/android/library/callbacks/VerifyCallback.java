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
package edu.vt.alerts.android.library.callbacks;

/**
 * A callback to be used by the RegistrationService's verifyDevice method. It is
 * used to communicate the results of the verification process.
 * 
 * All callback methods are called on the UI-thread.
 * 
 * @author Michael Irwin
 */
public interface VerifyCallback {

	/**
	 * The device is registered and is fully capable of receiving VT Alerts
	 * Push Notification messages.
	 */
	void deviceVerified();
	
	/**
	 * The device is no longer registered with the VT Alerts Push Notification
	 * system.
	 */
	void deviceNotRegistered();
	
	/**
	 * Callback used when an exception was thrown and verification was not able
	 * to be completed.
	 * @param e The exception that was thrown.
	 */
	void exceptionThrown(Exception e);
}
