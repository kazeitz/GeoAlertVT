/*
 * File created on Feb 28, 2014 
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

import edu.vt.alerts.android.library.domain.cap.CapAlert;

/**
 * Callback used by the AlertService whenever a list of Alert summaries is
 * requested.
 * 
 * @author Michael Irwin
 */
public interface AlertCallback {

	/**
	 * Callback used when the requested alert is retrieved.
	 * @param alerts The retrieved alert.
	 */
	void alertRetrieved(CapAlert alert);
	
	/**
	 * Callback used when the requested alert was unable to be retrieved, due to
	 * an exception.
	 * @param exception The exception that was thrown.
	 */
	void exceptionThrown(Exception exception);
	
}
