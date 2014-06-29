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

import java.util.List;

import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;

/**
 * Callback used by the AlertService whenever a list of Alert summaries is
 * requested.
 * 
 * @author Michael Irwin
 */
public interface AlertSummaryCallback {

	/**
	 * Callback used when the alerts were retrieved.
	 * @param alerts The retrieved alerts.
	 */
	void alertsRetrieved(List<AlertSummary> alerts);
	
	/**
	 * Callback used when the alerts were unable to be retrieved, due to an
	 * exception.
	 * @param exception The exception that was thrown.
	 */
	void exceptionThrown(Exception exception);
	
}
